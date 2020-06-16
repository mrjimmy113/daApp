package com.quang.daapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.ui.other.ExpertActivity;
import com.quang.daapp.ui.other.RequestListFragment;
import com.quang.daapp.ultis.WebSocketClient;

import java.util.List;

public class ExpertHomeFragment extends Fragment {

    private ExpertHomeViewModel viewModel;
    private boolean isProcessingOpen = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(ExpertHomeViewModel.class);

        return inflater.inflate(R.layout.fragment_home_expert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        final RequestListFragment fragProcessRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_process_request);

        assert fragProcessRequest != null;
        fragProcessRequest.setTitle("Processing request");
        fragProcessRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),true);
                navController.navigate(R.id.action_navigation_home_to_communicationFragment2,bundle);
            }
        });

        final RequestListFragment fragNewRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_new_request);
        assert fragNewRequest != null;
        fragNewRequest.setTitle("Applied request");
        fragNewRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {

            }
        });

        final RequestListFragment fragCancelRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_tmp_cancel_request);

        assert fragCancelRequest != null;
        fragCancelRequest.setTitle("Temperately canceled request");
        fragCancelRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),true);
                bundle.putInt("mode",3);
                navController.navigate(R.id.action_navigation_home_to_requestFinalInforFragment2,bundle);
            }
        });

        final RequestListFragment fragCompleteRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_tmp_complete_request);


        assert fragCompleteRequest != null;
        fragCompleteRequest.setTitle("Temperately completed request");
        fragCompleteRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),true);
                bundle.putInt("mode",2);
                navController.navigate(R.id.action_navigation_home_to_requestFinalInforFragment2,bundle);
            }
        });



        viewModel.getCurrentUserProcessingRequest();
        viewModel.getCurrentUserAppliedRequest();
        viewModel.getProcessingRequestListResult().observe(getViewLifecycleOwner(), new Observer<List<ProblemRequest>>() {
            @Override
            public void onChanged(List<ProblemRequest> problemRequests) {
                fragProcessRequest.setList(problemRequests);
                for (ProblemRequest p:
                        problemRequests) {
                    WebSocketClient.getInstance().subscribe(p.getRequestId());
                }
                ((ExpertActivity) getActivity()).startSub();
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

        viewModel.getCurrentUserTmpCancelRequest();
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
                    view.findViewById(R.id.container_tmp_cancel).setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getCurrentUserTmpCompleteRequest();
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
                    view.findViewById(R.id.container_tmp_complete).setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
