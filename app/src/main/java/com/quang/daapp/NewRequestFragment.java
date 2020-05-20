package com.quang.daapp;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewRequestFragment extends Fragment {

    DatePickerDialog picker;
    TextView txtEndDate;
    Date choosenDate;

    public NewRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_request, container, false);
    }



    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageButton btnChooseDate = view.findViewById(R.id.btnChooseDate);
        txtEndDate = view.findViewById(R.id.txtEnđate);
        changeDateDisplay(new Date(Calendar.getInstance().getTimeInMillis()));
        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                cldr.setTime(choosenDate);
                final int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                cldr.set(Calendar.YEAR,year);
                                cldr.set(Calendar.MONTH,month);
                                cldr.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                changeDateDisplay(new Date(cldr.getTimeInMillis()));
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    private void changeDateDisplay(Date date) {
        choosenDate = date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.format(date);
        txtEndDate.setText(date.toString());
    }
}
