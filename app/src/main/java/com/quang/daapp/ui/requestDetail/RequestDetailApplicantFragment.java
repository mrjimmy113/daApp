package com.quang.daapp.ui.requestDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ui.viewAdapter.ExpertInforAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailApplicantFragment extends Fragment {

    List<Expert> expertList = new ArrayList<>();
    private ProblemRequestDetailViewModel viewModel;
    Integer requestId;
    ExpertInforAdapter adapter;
    NavController navController;

    public RequestDetailApplicantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this)
                .get(ProblemRequestDetailViewModel.class);
        return inflater.inflate(R.layout.fragment_request_detail_applicant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.recycle);
        navController = Navigation.findNavController(getParentFragment().getView());
        adapter = new ExpertInforAdapter(getContext(), expertList, new ExpertInforAdapter.OnExpertInforInterface() {
            @Override
            public void OnProfileClickListener(int position) {
                final int expertId = expertList.get(position).getId();
                ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment(getString(R.string.mes_accept_expert_confirm)
                        , new ConfirmDialogFragment.OnConfirmDialogListener() {
                    @Override
                    public void OnYesListener() {
                        final LoaderDialogFragment loaderDialogFragment = new LoaderDialogFragment();
                        loaderDialogFragment.show(getParentFragmentManager(),getTag());
                        viewModel.acceptExpert(requestId,expertId);
                        viewModel.getAcceptExpertResult().observe(getViewLifecycleOwner(), new Observer<Number>() {
                            @Override
                            public void onChanged(Number number) {
                                loaderDialogFragment.dismiss();
                                if(number == null) return;
                                int value = number.intValue();
                                switch (value) {
                                    case 200: {
                                        MessageDialogFragment mesDialog = new MessageDialogFragment(
                                                getString(R.string.mes_accept_expert_success), R.color.colorSuccess, R.drawable.ic_success
                                                , new MessageDialogFragment.OnMyDialogListener() {
                                            @Override
                                            public void OnOKListener() {
                                                navController.navigate(R.id.navigation_home_customer);
                                            }
                                        }
                                        );
                                        mesDialog.show(getParentFragmentManager(),getTag());

                                        break;
                                    }

                                    case 400: {
                                        MessageDialogFragment mesDialog = new MessageDialogFragment(
                                                getString(R.string.mes_error_400),R.color.colorDanger, R.drawable.ic_error
                                        );
                                        mesDialog.show(getParentFragmentManager(),getTag());
                                        break;
                                    }
                                }
                            }
                        });

                    }

                    @Override
                    public void OnNoListener() {

                    }
                });
                dialogFragment.show(getParentFragmentManager(),getTag());
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        if(getArguments() != null) {
            requestId = getArguments().getInt(getString(R.string.key_request_id));
            viewModel.getApplicantOfRequest(requestId);
            viewModel.getApplicantResult().observe(getViewLifecycleOwner(), new Observer<List<Expert>>() {
                @Override
                public void onChanged(List<Expert> experts) {
                    if(experts == null) return;;
                    expertList = experts;
                    adapter.setExpertList(expertList);
                    adapter.notifyDataSetChanged();
                }
            });
        }



    }
}
