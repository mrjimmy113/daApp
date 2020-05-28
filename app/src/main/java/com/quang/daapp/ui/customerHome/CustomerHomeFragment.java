package com.quang.daapp.ui.customerHome;

import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.ui.viewAdapter.ProblemRequestAdapter;

import java.util.List;

public class CustomerHomeFragment extends Fragment {

    private CustomerHomeViewModel viewModel;

    private RecyclerView recyclerView;
    private ProblemRequestAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(CustomerHomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home_customer, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        final Button btnNewRequest = view.findViewById(R.id.btnNewRequest);
        recyclerView = view.findViewById(R.id.rv_problem_request);

        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_newRequestFragment);
            }
        });

        viewModel.getCurrentUserRequest();
        viewModel.getRequestList().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
            @Override
            public void onChanged(List<ProblemRequest> problemRequests) {
                adapter = new ProblemRequestAdapter(getView().getContext(), problemRequests, new ProblemRequestAdapter.ProblemRequestAdapterEvent() {
                    @Override
                    public void OnMainLayoutClick(int id) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(getString(R.string.key_request_id),id);
                        navController.navigate(R.id.action_navigation_home_to_problemRequestDetail,bundle);
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
            }
        });



    }
}