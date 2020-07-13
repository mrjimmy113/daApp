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
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProblemRequestDetailFragment extends Fragment {

    private ProblemRequestDetailViewModel viewModel;
    private NavController navController;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    private boolean isExpert = false;
    ProblemRequestDetail detail;

    public ProblemRequestDetailFragment() {
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
        final Integer requestId = getArguments().getInt(getString(R.string.key_request_id));
        Boolean expert = getArguments().getBoolean(getString(R.string.isExpert));
        Boolean viewOnly = getArguments().getBoolean("viewOnly");
        isExpert = expert;
        viewModel = new ViewModelProvider(this)
                .get(ProblemRequestDetailViewModel.class);
        final RequestDetailInforFragment infor =  new RequestDetailInforFragment();
        final RequestDetailImageFragment image = new RequestDetailImageFragment();
        final ImageButton btnEdit = view.findViewById(R.id.btnEditRequest);
        final ImageButton btnApply = view.findViewById(R.id.btnApply);
        navController = Navigation.findNavController(view);
        final ImageButton btnBack = view.findViewById(R.id.btnBack);


        viewModel.getProblemProquestDetail(requestId);
        viewModel.getDetailLive().observe(getViewLifecycleOwner(), new Observer<ProblemRequestDetail>() {
            @Override
            public void onChanged(ProblemRequestDetail problemRequestDetail) {
                if(problemRequestDetail == null) return;
                detail = problemRequestDetail;
                infor.setData(problemRequestDetail);
                image.setDate(problemRequestDetail.getImages());
            }
        });

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        final DetailPageAdapter adapter = new DetailPageAdapter(this);
        adapter.addFragment(infor,"Information");
        adapter.addFragment(image,"Image");
        if(!isExpert) {
            RequestDetailApplicantFragment applicant =  new RequestDetailApplicantFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.key_request_id),requestId);
            applicant.setArguments(bundle);
            adapter.addFragment(applicant,"Applicant");

        }else {
            btnEdit.setVisibility(View.GONE);
            btnApply.setVisibility(View.VISIBLE);
        }
        if(viewOnly) {
            btnApply.setVisibility(View.GONE);
        }
        viewPager.setAdapter(adapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(adapter.getTitle(position));
            }
        });
        mediator.attach();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Gson gson = new GsonBuilder().create();
                bundle.putString("data", gson.toJson(detail));
                navController.navigate(R.id.editRequestFragment,bundle);
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialogFragment confirmDialog = new ConfirmDialogFragment(getString(R.string.mes_expert_apply_confirm),
                        new ConfirmDialogFragment.OnConfirmDialogListener() {
                    @Override
                    public void OnYesListener() {
                        final LoaderDialogFragment loader = new LoaderDialogFragment();
                        loader.show(getParentFragmentManager(),getTag());
                        viewModel.applyResult(requestId);
                        viewModel.getApplyResult().observe(getViewLifecycleOwner(), new Observer<Number>() {
                            @Override
                            public void onChanged(Number number) {
                                loader.dismiss();
                                if(number == null) return;

                                int value = number.intValue();
                                switch (value) {
                                    case 200: {
                                        MessageDialogFragment mesDialog = new MessageDialogFragment(
                                                getString(R.string.mes_expert_apply_success),
                                                R.color.colorSuccess, R.drawable.ic_success
                                                , new MessageDialogFragment.OnMyDialogListener() {
                                            @Override
                                            public void OnOKListener() {
                                                navController.navigate(R.id.navigation_home);
                                            }
                                        });
                                        mesDialog.show(getParentFragmentManager(),getTag());
                                        break;
                                    }
                                    case 202: {
                                        MessageDialogFragment mesDialog = new MessageDialogFragment(
                                                getString(R.string.mes_expert_apply_fail),
                                                R.color.colorDanger,R.drawable.ic_error
                                        );
                                        mesDialog.show(getParentFragmentManager(),getTag());
                                        break;
                                    }
                                    case 400: {
                                        MessageDialogFragment mesDialog = new MessageDialogFragment(
                                                getString(R.string.mes_error_400),
                                                R.color.colorDanger,R.drawable.ic_error
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

                confirmDialog.show(getParentFragmentManager(),getTag());
            }
        });
    }
}

