package com.quang.daapp.ui.customerProfileEdit;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.service.RetrofitClient;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ultis.CommonUltis;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditCustomerProfileFragment extends Fragment {

    TextView txtEndDate;
    private Date choosenDate;
    private Uri choosenAvatar;
    ImageView ivAvatar;
    Customer data;

    private EditCustomerProfileViewModel viewModel;

    public EditCustomerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUltis.checkPermissions(getContext(),getActivity());
        }

        return inflater.inflate(R.layout.fragment_edit_customer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this)
                .get(EditCustomerProfileViewModel.class);




        final NavController navController = Navigation.findNavController(view);
        final ImageButton btnBack = view.findViewById(R.id.btnBack);
        final ImageButton btnSubmit = view.findViewById(R.id.btnSubmit);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        final EditText edtFullName = view.findViewById(R.id.edtFullName);
        final EditText edtAddress = view.findViewById(R.id.edtAddress);
        final Spinner spnCity = view.findViewById(R.id.spn_city);
        final Spinner spnPrimaryLanguage = view.findViewById(R.id.spn_primary_language);
        final ImageView btnChooseDate = view.findViewById(R.id.btnChooseDate);
        txtEndDate = view.findViewById(R.id.txtEndDate);

        Gson gson = new GsonBuilder().create();
        Bundle bundle = getArguments();
        Calendar cldr = Calendar.getInstance();
        changeDateDisplay(new Date(cldr.getTimeInMillis()));
        if(bundle != null) {
            String json = bundle.getString("data");
            data = gson.fromJson(json,Customer.class);

            if(data.getFullName() != null && !data.getFullName().trim().isEmpty()) {
                edtFullName.setText(data.getFullName());
            }
            if(data.getAddress() != null && !data.getAddress().trim().isEmpty()) {
                edtAddress.setText(data.getAddress());
            }

            if(data.getDob() != null) {
                changeDateDisplay(data.getDob());
            }
            if(data.getCity() != null && !data.getCity().trim().isEmpty()) {
                String[] cities = getResources().getStringArray(R.array.city_arrays);
                for (int i = 0; i < cities.length;i++) {
                    if(cities[i].equals(data.getCity())) {
                        spnCity.setSelection(i);
                        break;
                    }
                }
            }
            if(data.getPrimaryLanguage() != null && !data.getPrimaryLanguage().trim().isEmpty()) {
                String[] languages = getResources().getStringArray(R.array.language_arrays);
                for (int i = 0; i < languages.length; i++) {
                    if (languages[i].equals(data.getPrimaryLanguage())) {
                        spnPrimaryLanguage.setSelection(i);
                        break;
                    }
                }
            }

            if(data.getImgName() != null && !data.getImgName().trim().isEmpty()) {
                Glide.with(view).load(RetrofitClient.getImageUrl(data.getImgName())).into(ivAvatar);
            }
        }

        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getContext(),
                R.array.city_arrays, android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(adapterCity);

        ArrayAdapter<CharSequence> adapterLanguage = ArrayAdapter.createFromResource(getContext(),
                R.array.language_arrays, android.R.layout.simple_spinner_item);
        adapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPrimaryLanguage.setAdapter(adapterLanguage);



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCustomerProfileFormState formState = viewModel.getFormState(edtFullName.getText().toString(), edtAddress.getText().toString());
                if(!formState.isDataValid()) {
                    MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_validate),R.color.colorDanger,R.drawable.ic_error);
                    mesDialog.show(getParentFragmentManager(),getTag());
                    if(formState.getFullNameError() != null) edtFullName.setError(getString(formState.getFullNameError()));
                    if(formState.getAddressError() != null) edtAddress.setError(getString(formState.getAddressError()));
                    return;
                }

                Customer customer = new Customer();
                customer.setFullName(edtFullName.getText().toString());
                customer.setAddress(edtAddress.getText().toString());
                customer.setCity(spnCity.getSelectedItem().toString());
                customer.setPrimaryLanguage(spnPrimaryLanguage.getSelectedItem().toString());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                customer.setDob(choosenDate);
                String filePath = null;
                if(choosenAvatar != null) {
                    filePath = CommonUltis.getPathFromURI(choosenAvatar,getActivity());
                }
                viewModel.editCustomer(filePath,customer);
                final LoaderDialogFragment loaderDialog = new LoaderDialogFragment();
                loaderDialog.show(getParentFragmentManager(),getTag());
                viewModel.getEditResult().observe(getViewLifecycleOwner(), new Observer<Number>() {
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
                                    navController.navigate(R.id.navigation_profile_customer);
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

    private void openDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        cldr.setTime(choosenDate);
        DatePickerDialog picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cldr.set(Calendar.YEAR, year);
                        cldr.set(Calendar.MONTH, month);
                        cldr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Date d = new Date(cldr.getTimeInMillis());
                        changeDateDisplay(d);
                    }
                }, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }



    private void changeDateDisplay(Date date) {
        choosenDate = date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String a = format.format(choosenDate);
        txtEndDate.setText(a);
    }

    private void pickFromGallery(){
        startActivityForResult(CommonUltis.getPickFromGalleryIntent(), CommonUltis.GALLERY_REQUEST_CODE);
    }

}
