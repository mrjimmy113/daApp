package com.quang.daapp.ui.customerProfileEdit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditCustomerProfileFragment extends Fragment {

    public EditCustomerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_customer_profile, container, false);
    }
}
