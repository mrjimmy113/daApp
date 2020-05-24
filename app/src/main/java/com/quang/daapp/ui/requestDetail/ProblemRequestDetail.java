package com.quang.daapp.ui.requestDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quang.daapp.R;
import com.quang.daapp.ui.newRequest.NewRequestViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProblemRequestDetail extends Fragment {

    private ProblemRequestDetailViewModel viewModel;
    private NavController navController;

    public ProblemRequestDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_problem_request_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this)
                .get(ProblemRequestDetailViewModel.class);
        navController = Navigation.findNavController(view);
        final Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.navigation_home);
            }
        });

        final Button btnEdit = view.findViewById(R.id.btnEditRequest);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
