package com.quang.daapp.ui.updateRequest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ui.viewAdapter.ImgChooserAdapter;
import com.quang.daapp.ultis.CommonUltis;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditRequestFragment extends Fragment {

    private TextView txtEndDate;
    private Date choosenDate;

    private Button btnChooseImg;
    private RecyclerView recyclerView;

    private TextView txtTotalLength;
    private long totalImageLength = 0;

    private ArrayList<String> imgURL = new ArrayList<>();
    private ImgChooserAdapter adapter;
    private List<Major> majorsResult;

    private EditRequestViewModel viewModel;

    private NavController navController;
    private ProblemRequestDetail detail;
    public EditRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUltis.checkPermissions(getContext(),getActivity());
        }
        return inflater.inflate(R.layout.fragment_new_request, container, false);
    }



    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        String json = getArguments().getString("data");
        detail = (new GsonBuilder().create()).fromJson(json,ProblemRequestDetail.class);


        navController = Navigation.findNavController(view);
        Calendar cldr = Calendar.getInstance();
        cldr.add(Calendar.DATE,2);
        txtEndDate = view.findViewById(R.id.txtEndDate);
        changeDateDisplay(new Date(detail.getDeadlineDate().getTime()));


        viewModel = ViewModelProviders.of(this)
                .get(EditRequestViewModel.class);

        final EditText edtTitle = view.findViewById(R.id.edtTitle);
        final EditText edtDescription = view.findViewById(R.id.edtDescription);

        final Spinner spnMajor = view.findViewById(R.id.spn_major);

        edtTitle.setText(detail.getTitle());
        edtDescription.setText(detail.getDescription());
        for (String img:
             detail.getImages()) {
            imgURL.add(NetworkClient.getImageUrl(img));
        }

        viewModel.getAllMajor();
        viewModel.getAllMajorResult().observe(getViewLifecycleOwner(), new Observer<List<Major>>() {
            @Override
            public void onChanged(List<Major> majors) {
                if(majors == null) return;

                majorsResult = majors;

                ArrayAdapter<Major> adapterCity = new ArrayAdapter<Major>(getContext(),android.R.layout.simple_spinner_item, majorsResult);
                adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnMajor.setAdapter(adapterCity);
                spnMajor.setSelection(detail.getMajor().getId() - 1);
            }
        });

        txtTotalLength = view.findViewById(R.id.txtTotalLength);
        final ImageButton btnChooseDate = view.findViewById(R.id.btnChooseDate);



        final ImageButton btnCreateReq = view.findViewById(R.id.btnCreateRequest);
        btnCreateReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoaderDialogFragment loaderDialog = new LoaderDialogFragment();
                loaderDialog.show(getParentFragmentManager(),getTag());

                viewModel.editRequest(adapter.getNewImg().toArray(new String[adapter.getNewImg().size()]),
                        detail.getRequestId(),
                        choosenDate,edtTitle.getText().toString(),edtDescription.getText().toString(),
                        ((Major)spnMajor.getSelectedItem()).getId(),
                        adapter.getOldImgDelete());
                viewModel.getEditRequestResult().observe(getViewLifecycleOwner(), new Observer<Number>() {
                    @Override
                    public void onChanged(Number number) {
                        loaderDialog.dismiss();
                        if(number == null) {
                            MessageDialogFragment dialog = new MessageDialogFragment(getString(R.string.mes_error_400), R.color.colorDanger,R.drawable.ic_error);
                            dialog.show(getParentFragmentManager(),getTag());
                            return;
                        }

                        if(number.intValue() == 200) {

                            MessageDialogFragment dialog = new MessageDialogFragment(getString(R.string.mes_edit_request_success),
                                    R.color.colorSuccess, R.drawable.ic_success, new MessageDialogFragment.OnMyDialogListener() {
                                @Override
                                public void OnOKListener() {
                                    navController.navigate(R.id.navigation_home_customer);
                                }
                            });
                            dialog.show(getParentFragmentManager(),getTag());
                        }
                    }
                });
            }
        });


        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        btnChooseImg = view.findViewById(R.id.btnChooseImg);
        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        recyclerView = view.findViewById(R.id.recyc_img);
        adapter = new ImgChooserAdapter(view.getContext(), imgURL, new ImgChooserAdapter.OnItemRemove() {
            @Override
            public void OnItemRemoved(int position) {
                addFileToList(imgURL.get(position),false);
                imgURL.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeRemoved(position,adapter.getItemCount());
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });
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
                        if (viewModel.isValidEndDate(d)) {
                            changeDateDisplay(d);
                        } else {
                            Toast.makeText(getActivity(), R.string.invalid_end_date, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1052: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    btnChooseImg.setEnabled(true);

                } else {


                    btnChooseImg.setEnabled(false);

                }
                return;
            }

        }
    }

    private void changeDateDisplay(Date date) {
        choosenDate = date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        txtEndDate.setText(format.format(choosenDate).toString());
    }



    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Launching the Intent
        startActivityForResult(intent, CommonUltis.GALLERY_REQUEST_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case CommonUltis.GALLERY_REQUEST_CODE:
                    addFileToList(CommonUltis.getPathFromURI(data.getData(),getActivity()), true);
                    break;
            }
    }

    private void addFileToList(String uri, boolean isAdd) {

        File file = new File(uri);

        if(!isAdd) {
            totalImageLength -= file.length();
            txtTotalLength.setText((double)(totalImageLength / 1024) + "");
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        }else {
            if(totalImageLength + file.length() < 5 * 1024 * 1024) {
                imgURL.add(uri);
                adapter.addNewImg(uri);
                adapter.notifyItemInserted(imgURL.size() - 1);
                totalImageLength += file.length();
                txtTotalLength.setText((double)(totalImageLength / 1024) + "");
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }else {
                Toast.makeText(getActivity(), R.string.invalid_img_over, Toast.LENGTH_SHORT).show();
            }
        }



    }



    private String[] getFilesRealPath(List<Uri> souceList) {
        String[] result = new String[souceList.size()];
        for(int i = 0 ; i < result.length;i++) {
            result[i] = CommonUltis.getPathFromURI(souceList.get(i), getActivity());
        }
        return result;
    }


}
