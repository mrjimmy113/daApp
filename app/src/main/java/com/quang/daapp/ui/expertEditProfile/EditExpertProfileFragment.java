package com.quang.daapp.ui.expertEditProfile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.service.RetrofitClient;
import com.quang.daapp.ui.customerProfileEdit.EditCustomerProfileFormState;
import com.quang.daapp.ui.customerProfileEdit.EditCustomerProfileViewModel;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ultis.CommonUltis;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditExpertProfileFragment extends Fragment {

    EditExpertProfileViewModel viewModel;
    private Uri choosenAvatar;
    ImageView ivAvatar;
    Expert data;
    NavController navController;

    private List<Major> majorsResult;

    public EditExpertProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(EditExpertProfileViewModel.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUltis.checkPermissions(getContext(),getActivity());
        }
        return inflater.inflate(R.layout.fragment_edit_expert_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        final EditText edtFullName = view.findViewById(R.id.edtFullName);
        final Spinner spnMajor = view.findViewById(R.id.spn_major);
        final EditText edtFee = view.findViewById(R.id.edtFee);
        final EditText edtBankName = view.findViewById(R.id.edtBankName);
        final EditText edtAccountNo = view.findViewById(R.id.edtAccountNo);
        final EditText edtDescription = view.findViewById(R.id.edtDescription);


        navController = Navigation.findNavController(view);

        Gson gson = new GsonBuilder().create();
        Bundle bundle = getArguments();
        String json = bundle.getString("data");
        data = gson.fromJson(json,Expert.class);
        if(data != null) {
            edtFullName.setText(data.getFullName());
            edtFee.setText(data.getFeeString());
            edtBankName.setText(data.getBankName());
            edtAccountNo.setText(data.getBankAccountNo());
            edtDescription.setText(data.getDescription());
            Glide.with(getContext()).load(RetrofitClient.getImageUrl(data.getImgName())).into(ivAvatar);
        }


        viewModel.getAllMajor();
        viewModel.getAllMajorResult().observe(getViewLifecycleOwner(), new Observer<List<Major>>() {
            @Override
            public void onChanged(List<Major> majors) {
                if(majors == null) return;

                majorsResult = majors;
                ArrayAdapter<Major> adapterCity = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, majorsResult);
                adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnMajor.setAdapter(adapterCity);
                if(data != null) {
                    spnMajor.setSelection(data.getMajor().getId() - 1);
                }
            }
        });





        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        view.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditExpertProfileFormState formState = viewModel.validateDate(
                        edtFullName.getText().toString(),
                        edtFee.getText().toString(),
                        edtBankName.getText().toString(),
                        edtAccountNo.getText().toString()
                );
                if(!formState.isDataValid()) {
                    MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_validate),R.color.colorDanger,R.drawable.ic_error);
                    mesDialog.show(getParentFragmentManager(),getTag());
                    if(formState.getFullNameError() != null) edtFullName.setError(getString(formState.getFullNameError()));
                    if(formState.getFeeError() != null) edtFee.setError(getString(formState.getFeeError()));
                    if(formState.getBankAccount() != null) edtBankName.setError(getString(formState.getBankAccount()));
                    if(formState.getAccountNo() != null) edtAccountNo.setError(getString(formState.getAccountNo()));
                    return;
                }

                Expert expert = new Expert();
                expert.setFullName(edtFullName.getText().toString());
                expert.setMajor((Major) spnMajor.getSelectedItem());
                expert.setFeePerHour(Float.parseFloat(edtFee.getText().toString()));
                expert.setBankName(edtBankName.getText().toString());
                expert.setBankAccountNo(edtAccountNo.getText().toString());
                expert.setDescription(edtDescription.getText().toString());

                String filePath = null;
                if(choosenAvatar != null) {
                    filePath = CommonUltis.getPathFromURI(choosenAvatar,getActivity());
                }
                viewModel.updateExpert(filePath,expert);
                final LoaderDialogFragment loaderDialog = new LoaderDialogFragment();
                loaderDialog.show(getParentFragmentManager(),getTag());
                viewModel.getUpdateResult().observe(getViewLifecycleOwner(), new Observer<Number>() {
                    @Override
                    public void onChanged(Number number) {
                        loaderDialog.dismiss();
                        if(number == null) {
                            MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_400),R.color.colorDanger,R.drawable.ic_error);
                            mesDialog.show(getParentFragmentManager(),getTag());
                            return;
                        }

                        if(number.intValue() == 200) {
                            MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_edit_profile_success),
                                    R.color.colorSuccess, R.drawable.ic_success, new MessageDialogFragment.OnMyDialogListener() {
                                @Override
                                public void OnOKListener() {
                                    navController.popBackStack();
                                }
                            });
                            mesDialog.show(getParentFragmentManager(),getTag());
                        }else if(number.intValue() == 400) {
                            MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_400),R.color.colorDanger,R.drawable.ic_error);
                            mesDialog.show(getParentFragmentManager(),getTag());
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case CommonUltis.GALLERY_REQUEST_CODE:
                    setAvatar(data.getData());
                    break;
            }
    }

    private void setAvatar(Uri uri) {
        File file = new File(CommonUltis.getPathFromURI(uri,getActivity()));
        if(file.length() < 5 * 1024 * 1024) {
            choosenAvatar = uri;
            ivAvatar.setImageURI(uri);
        }
    }

    private void pickFromGallery(){
        startActivityForResult(CommonUltis.getPickFromGalleryIntent(), CommonUltis.GALLERY_REQUEST_CODE);
    }
}
