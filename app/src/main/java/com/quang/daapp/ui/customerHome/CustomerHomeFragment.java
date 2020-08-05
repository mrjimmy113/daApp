package com.quang.daapp.ui.customerHome;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.ui.other.CustomerActivity;
import com.quang.daapp.ui.other.ExpertActivity;
import com.quang.daapp.ui.other.RequestListFragment;
import com.quang.daapp.ultis.WebSocketClient;

import java.util.List;

public class CustomerHomeFragment extends Fragment {

    private CustomerHomeViewModel viewModel;
    private boolean isProcessingOpen = false;
    private int pageAccept = 0;
    private int pageTmpCancel = 0;
    private int pageTmpComplete = 0;
    private int pageNew = 0;
    private RequestListFragment fragCancelRequest;
    private RequestListFragment fragCompleteRequest;
    private  RequestListFragment fragNewRequest;
    private RequestListFragment fragProcessRequest;
    private View containerComplete;
    private View containerCancel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CustomerHomeViewModel.class);


        return inflater.inflate(R.layout.fragment_home_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        final Button btnNewRequest = view.findViewById(R.id.btnNewRequest);
        containerCancel =  view.findViewById(R.id.container_tmp_cancel);
        containerComplete = view.findViewById(R.id.container_tmp_complete);
        fragProcessRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_process_request);

        assert fragProcessRequest != null;
        fragProcessRequest.setTitle("Ongoing request");
        fragProcessRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),false);
                navController.navigate(R.id.action_navigation_home_customer_to_customerCommunicationFragment,bundle);
            }

            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                viewModel.getCurrentUserAcceptedRequest(pageAccept + 1);
                return viewModel.getAcceptedRequestList();
            }

            @Override
            public void  OnGetRequestSuccess(boolean isSuccess) {
                if(isSuccess) pageAccept++;
            }
        });

        fragCancelRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_tmp_cancel_request);


        assert fragCancelRequest != null;
        fragCancelRequest.setTitle("Temperately canceled request");
        fragCancelRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),false);
                navController.navigate(R.id.action_navigation_home_customer_to_customerCommunicationFragment,bundle);
                /*Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                navController.navigate(R.id.action_navigation_home_to_problemRequestDetail,bundle);*/
                /*Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),false);
                bundle.putInt("mode",3);
                navController.navigate(R.id.action_navigation_home_customer_to_requestFinalInforFragment,bundle);*/
            }
            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                viewModel.getCurrentUserAcceptedRequest(pageTmpCancel + 1);
                return viewModel.getAcceptedRequestList();
            }

            @Override
            public void  OnGetRequestSuccess(boolean isSuccess) {
                if(isSuccess) pageTmpCancel++;
            }
        });

        fragCompleteRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_tmp_complete_request);


        assert fragCompleteRequest != null;
        fragCompleteRequest.setTitle("Temperately completed request");
        fragCompleteRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),false);
                navController.navigate(R.id.action_navigation_home_customer_to_customerCommunicationFragment,bundle);
                /*Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                navController.navigate(R.id.action_navigation_home_to_problemRequestDetail,bundle);*/
                /*Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),false);
                bundle.putInt("mode",2);
                navController.navigate(R.id.action_navigation_home_customer_to_requestFinalInforFragment,bundle);*/
            }
            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                viewModel.getCurrentUserAcceptedRequest(pageTmpComplete + 1);
                return viewModel.getAcceptedRequestList();
            }

            @Override
            public void  OnGetRequestSuccess(boolean isSuccess) {
                if(isSuccess) pageTmpComplete++;
            }
        });

        fragNewRequest =
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

            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                viewModel.getCurrentUserAcceptedRequest(pageNew + 1);
                return viewModel.getAcceptedRequestList();
            }

            @Override
            public void  OnGetRequestSuccess(boolean isSuccess) {
                if(isSuccess) pageNew++;
            }
        });

        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_newRequestFragment);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getCurrentUserAcceptedRequest(pageAccept);
        viewModel.getCurrentUserNewRequest(pageNew);
        viewModel.getAcceptedRequestList().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
            @Override
            public void onChanged(List<ProblemRequest> problemRequests) {
                if(problemRequests == null) return;
                fragProcessRequest.setList(problemRequests);
                for (ProblemRequest p:
                        problemRequests) {
                    WebSocketClient.getInstance().subscribe(p.getRequestId());
                }

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

        viewModel.getCurrentUserTmpCancelRequest(pageTmpCancel);
        viewModel.getTmpCancelRequestList().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
            @Override
            public void onChanged(List<ProblemRequest> problemRequests) {
                if(problemRequests == null) return;
                fragCancelRequest.setList(problemRequests);
                for (ProblemRequest p:
                        problemRequests) {
                    WebSocketClient.getInstance().subscribe(p.getRequestId());
                }
                if(problemRequests.size() > 0) {
                    containerCancel.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getCurrentUserTmpCompleteRequest(pageTmpComplete);
        viewModel.getTmpCompleteRequestList().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
            @Override
            public void onChanged(List<ProblemRequest> problemRequests) {
                if(problemRequests == null) return;
                fragCompleteRequest.setList(problemRequests);
                for (ProblemRequest p:
                        problemRequests) {
                    WebSocketClient.getInstance().subscribe(p.getRequestId());
                }
                if(problemRequests.size() > 0) {
                    containerComplete.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
