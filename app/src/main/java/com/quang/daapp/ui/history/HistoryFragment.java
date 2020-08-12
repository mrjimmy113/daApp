package com.quang.daapp.ui.history;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.ui.other.ExpertActivity;
import com.quang.daapp.ui.other.RequestListFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    private HistoryViewModel viewModel;
    private boolean isExpert = false;

    private RequestListFragment completeRequest;
    private RequestListFragment cancelRequest;

    private int pageComplete = 0;
    private int pageCancel = 0;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        if(getActivity() instanceof ExpertActivity) {
            isExpert = true;
        }
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        completeRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_complete_request);

        cancelRequest =
                (RequestListFragment) getChildFragmentManager().findFragmentById(R.id.frag_cancel_request);


        assert completeRequest != null;
        completeRequest.setTitle("Completed request");
        completeRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),isExpert);
                if(isExpert) {
                    navController.navigate(R.id.action_navigation_history_to_requestFinalInforFragment2,bundle);
                }else {
                    navController.navigate(R.id.action_navigation_history_to_requestFinalInforFragment,bundle);
                }
            }

            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                viewModel.getCompleteRequest(pageComplete + 1);
                return viewModel.getCompleteRequestResult();
            }
            @Override
            public void  OnGetRequestSuccess(boolean isSuccess) {
                if(isSuccess) pageComplete++;
            }
        });

        assert cancelRequest != null;
        cancelRequest.setTitle("Canceled request");
        cancelRequest.setEvent(new RequestListFragment.OnRequestListListener() {
            @Override
            public void OnRequestClickListener(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.key_request_id), id);
                bundle.putBoolean(getString(R.string.isExpert),isExpert);
                bundle.putInt("mode", 1);
                if(isExpert) {
                    navController.navigate(R.id.action_navigation_history_to_requestFinalInforFragment2,bundle);
                }else {
                    navController.navigate(R.id.action_navigation_history_to_requestFinalInforFragment,bundle);
                }
            }

            @Override
            public LiveData<List<ProblemRequest>> OnScrollToBottom() {
                viewModel.getCancelRequest(pageCancel + 1);
                return viewModel.getCancelRequestResult();
            }

            @Override
            public void  OnGetRequestSuccess(boolean isSuccess) {
                if(isSuccess) pageCancel++;
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getCompleteRequest(pageComplete);
        viewModel.getCompleteRequestResult().observe(getViewLifecycleOwner(), problemRequests -> {
            if (problemRequests == null) return;
            completeRequest.setList(problemRequests);
        });

        viewModel.getCancelRequest(pageCancel);
        viewModel.getCancelRequestResult().observe(getViewLifecycleOwner(), problemRequests -> {
            if (problemRequests == null) return;
            cancelRequest.setList(problemRequests);
        });
    }
}
