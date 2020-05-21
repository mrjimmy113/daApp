package com.quang.daapp.ui.newRequest;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.quang.daapp.R;
import com.quang.daapp.ui.viewAdapter.ImgChooserAdapter;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewRequestFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 0;
    DatePickerDialog picker;
    TextView txtEndDate;
    Date choosenDate;

    Button btnChooseImg;
    RecyclerView recyclerView;

    TextView txtTotalLength;
    private long totalImageLength = 0;

    ArrayList<String> imgURL = new ArrayList<>();
    ImgChooserAdapter adapter;

    NewRequestViewModel viewModel;

    public NewRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        return inflater.inflate(R.layout.fragment_new_request, container, false);
    }



    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar cldr = Calendar.getInstance();
        cldr.add(Calendar.DATE,2);
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        choosenDate = new Date(cldr.getTimeInMillis());


        viewModel = ViewModelProviders.of(this)
                .get(NewRequestViewModel.class);

        final EditText edtTitle = view.findViewById(R.id.edtTitle);
        final EditText edtDescription = view.findViewById(R.id.edtDescription);

        viewModel.getNewRequestFormState().observe(getViewLifecycleOwner(), new Observer<NewRequestFormState>() {
            @Override
            public void onChanged(NewRequestFormState newRequestFormState) {
                if(newRequestFormState == null) return;
                if(newRequestFormState.getTitleError() != null) {
                    edtTitle.setError(newRequestFormState.getTitleError().toString());
                }
                if(newRequestFormState.getDescriptionError() != null) {
                    edtDescription.setError(newRequestFormState.getDescriptionError().toString());
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.onDataChange(edtTitle.getText().toString(),edtDescription.getText().toString(),choosenDate,totalImageLength);
            }
        };

        txtTotalLength = view.findViewById(R.id.txtTotalLength);
        final ImageButton btnChooseDate = view.findViewById(R.id.btnChooseDate);
        txtEndDate = view.findViewById(R.id.txtEnđate);
        changeDateDisplay(new Date(Calendar.getInstance().getTimeInMillis()));

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
    }

    private void openDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        cldr.setTime(choosenDate);
        picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cldr.set(Calendar.YEAR,year);
                        cldr.set(Calendar.MONTH,month);
                        cldr.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        Date d = new Date(cldr.getTimeInMillis());
                        if(viewModel.isValidEndDate(d)) {
                            changeDateDisplay(d);
                        }else {
                            Toast.makeText(getActivity(), R.string.invalid_end_date, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    private void checkPermissions(){

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);

        }

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
        format.format(date);
        txtEndDate.setText(date.toString());
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
        startActivityForResult(intent,GALLERY_REQUEST_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    addFileToList(picturePath, true);
                    break;
            }
    }

    private void addFileToList(String path, boolean isAdd) {

        File file = new File(path);

        if(!isAdd) {
            totalImageLength -= file.length();
            txtTotalLength.setText((double)(totalImageLength / 1024) + "");
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        }

        if(totalImageLength + file.length() < 5 * 1024 * 1024 && isAdd) {
            imgURL.add(path);
            adapter.notifyItemInserted(imgURL.size() - 1);
            totalImageLength += file.length();
            txtTotalLength.setText((double)(totalImageLength / 1024) + "");
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        }else {
            Toast.makeText(getActivity(), R.string.invalid_img_over, Toast.LENGTH_SHORT).show();
        }

    }


}
