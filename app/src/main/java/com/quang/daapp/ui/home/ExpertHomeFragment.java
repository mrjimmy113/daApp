package com.quang.daapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ExpertHomeFragment extends Fragment {

    private ExpertHomeViewModel viewModel;
    private boolean isProcessingOpen = false;
    private int pageProcess = 0;
    private int pageCancel = 0;
    private int pageComplete = 0;
    private RequestListFragment fragProcessRequest;
    private RequestListFragment fragNewRequest;
    private RequestListFragment fragCompleteRequest;
    private RequestListFragment fragCancelRequest;
    private View containerComplete;
    private View containerCancel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(ExpertHomeViewModel.class);

        return inflater.inflate(R.layout.fragment_home_expert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        containerComplete =  view.findViewById(R.id.container_tmp_complete);
        containerCancel = view.findViewById(R.id.container_tmp_cancel);
        fragProcessRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_process_request);

        assert fragProcessRequest != null;
        fragProcessRequest.setTitle("Ongoing request");
        fragProcessRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),true);
                navController.navigate(R.id.action_navigation_home_to_communicationFragment2,bundle);
            }

            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                viewModel.getCurrentUserProcessingRequest(pageProcess + 1);
                return viewModel.getProcessingRequestListResult();
            }

            @Override
            public void OnGetRequestSuccess(boolean isSuccess) {

            }
        });

        fragNewRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_new_request);
        assert fragNewRequest != null;
        fragNewRequest.setTitle("Applied request");
        fragNewRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {

            }

            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                return null;
            }

            @Override
            public void OnGetRequestSuccess(boolean isSuccess) {

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
                bundle.putBoolean(getString(R.string.isExpert),true);
                navController.navigate(R.id.action_navigation_home_to_communicationFragment2,bundle);
               /* Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),true);
                navController.navigate(R.id.action_navigation_home_to_communicationFragment2,bundle);*/
                /*Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),true);
                bundle.putInt("mode",3);
                navController.navigate(R.id.action_navigation_home_to_requestFinalInforFragment2,bundle);*/
            }

            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                viewModel.getCurrentUserTmpCancelRequest(pageCancel +1);
                return viewModel.getTmpCancelRequestList();
            }

            @Override
            public void OnGetRequestSuccess(boolean isSuccess) {
                if(isSuccess) pageCancel++;
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
                bundle.putBoolean(getString(R.string.isExpert),true);
                navController.navigate(R.id.action_navigation_home_to_communicationFragment2,bundle);
                /*Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),true);
                navController.navigate(R.id.action_navigation_home_to_communicationFragment2,bundle);*/
/*                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),true);
                bundle.putInt("mode",2);
                navController.navigate(R.id.action_navigation_home_to_requestFinalInforFragment2,bundle);*/
            }

            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                viewModel.getCurrentUserTmpCompleteRequest(pageComplete + 1);
                return viewModel.getTmpCompleteRequestList();
            }

            @Override
            public void OnGetRequestSuccess(boolean isSuccess) {
                if(isSuccess) pageComplete++;
            }
        });




    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getCurrentUserProcessingRequest(pageProcess);
        viewModel.getCurrentUserAppliedRequest();
        viewModel.getProcessingRequestListResult().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
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
                viewModel.getAppliedRequestListResult().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
                    @Override
                    public void onChanged(List<ProblemRequest> problemRequests) {
                        fragNewRequest.setList(problemRequests);
                        if(!isProcessingOpen) fragNewRequest.openClose();
                    }
                });
            }
        });

        viewModel.getCurrentUserTmpCancelRequest(pageCancel);
        viewModel.getTmpCancelRequestList().observe(getViewLifecycleOwner(), problemRequests -> {
            if(problemRequests == null) return;
            fragCancelRequest.setList(problemRequests);
            for (ProblemRequest p:
                    problemRequests) {
                WebSocketClient.getInstance().subscribe(p.getRequestId());
            }
            if(problemRequests.size() > 0) {
                containerCancel.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getCurrentUserTmpCompleteRequest(pageComplete);
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
        ((ExpertActivity)getActivity()).findSub();
    }
}
