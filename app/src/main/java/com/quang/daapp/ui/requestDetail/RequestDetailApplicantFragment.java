package com.quang.daapp.ui.requestDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ui.viewAdapter.ExpertInforAdapter;
import com.quang.daapp.ultis.NetworkClient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailApplicantFragment extends Fragment {

    private List<Expert> expertList = new ArrayList<>();
    private ProblemRequestDetailViewModel viewModel;
    private Integer requestId;
    private ExpertInforAdapter adapter;
    private NavController navController;
    private TextView txtEmpty;

    public RequestDetailApplicantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this)
                .get(ProblemRequestDetailViewModel.class);
        return inflater.inflate(R.layout.fragment_request_detail_applicant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.recycle);
        navController = Navigation.findNavController(getParentFragment().getView());
        txtEmpty = view.findViewById(R.id.txtEmpty);

        adapter = new ExpertInforAdapter(getContext(), expertList, new ExpertInforAdapter.OnExpertInforInterface() {
            @Override
            public void OnProfileClickListener(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("detail", NetworkClient.getInstance().getGson().toJson(expertList.get(position)));
                bundle.putInt(getString(R.string.key_request_id), requestId);
                navController.navigate(R.id.action_problemRequestDetail_to_expertStatFragment,bundle);

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));




    }

    @Override
    public void onResume() {
        super.onResume();
        if(getArguments() != null) {
            requestId = getArguments().getInt(getString(R.string.key_request_id));
            viewModel.getApplicantOfRequest(requestId);
            viewModel.getApplicantResult().observe(getViewLifecycleOwner(), new Observer<List<Expert>>() {
                @Override
                public void onChanged(List<Expert> experts) {
                    if(experts == null) return;;
                    if(experts.isEmpty()) txtEmpty.setVisibility(View.VISIBLE);
                    txtEmpty.setVisibility(View.GONE);
                    expertList = experts;
                    adapter.setExpertList(expertList);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
