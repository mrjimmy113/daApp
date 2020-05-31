package com.quang.daapp.ui.customerHome;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.quang.daapp.R;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.ui.other.RequestListFragment;

import java.util.List;

public class CustomerHomeFragment extends Fragment {

    private CustomerHomeViewModel viewModel;
    private boolean isProcessingOpen = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(CustomerHomeViewModel.class);

        return inflater.inflate(R.layout.fragment_home_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        final Button btnNewRequest = view.findViewById(R.id.btnNewRequest);
        final RequestListFragment fragProcessRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_process_request);

        assert fragProcessRequest != null;
        fragProcessRequest.setTitle("Processing request");
        fragProcessRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                
            }
        });

        final RequestListFragment fragNewRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_new_request);
        assert fragNewRequest != null;
        fragNewRequest.setTitle("New request");
        fragNewRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                navController.navigate(R.id.action_navigation_home_to_problemRequestDetail,bundle);
            }
        });

        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_newRequestFragment);
            }
        });

        viewModel.getCurrentUserProcessingRequest();
        viewModel.getCurrentUserNewRequest();
        viewModel.getProcessingRequestList().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
            @Override
            public void onChanged(List<ProblemRequest> problemRequests) {
                fragProcessRequest.setList(problemRequests);
                if(problemRequests.size() > 0) {
                    fragProcessRequest.openClose();
                    isProcessingOpen = true;
                }
                viewModel.getNewRequestList().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
                    @Override
                    public void onChanged(List<ProblemRequest> problemRequests) {
                        fragNewRequest.setList(problemRequests);
                        if(!isProcessingOpen) fragNewRequest.openClose();
                    }
                });
            }
        });

    }
}
