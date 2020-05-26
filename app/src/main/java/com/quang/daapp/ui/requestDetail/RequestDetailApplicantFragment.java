package com.quang.daapp.ui.requestDetail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailApplicantFragment extends Fragment {

    public RequestDetailApplicantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_detail_applicant, container, false);
    }
}
