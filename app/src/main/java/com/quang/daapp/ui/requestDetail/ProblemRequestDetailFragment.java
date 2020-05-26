package com.quang.daapp.ui.requestDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.quang.daapp.R;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.ui.newRequest.NewRequestViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProblemRequestDetailFragment extends Fragment {

    private ProblemRequestDetailViewModel viewModel;
    private NavController navController;
    ViewPager2 viewPager;
    TabLayout tabLayout;
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
        Integer requestId = getArguments().getInt(getString(R.string.key_request_id));
        viewModel = ViewModelProviders.of(this)
                .get(ProblemRequestDetailViewModel.class);
        final RequestDetailInforFragment infor =  new RequestDetailInforFragment();
        final RequestDetailImageFragment image = new RequestDetailImageFragment();
        RequestDetailApplicantFragment applicant =  new RequestDetailApplicantFragment();
        if(requestId != null) {
            viewModel.getProblemProquestDetail(requestId);
            viewModel.getDetailLive().observe(getViewLifecycleOwner(), new Observer<ProblemRequestDetail>() {
                @Override
                public void onChanged(ProblemRequestDetail problemRequestDetail) {
                    if(problemRequestDetail == null) return;


                    infor.setData(problemRequestDetail);
                    image.setDate(problemRequestDetail.getImages());


                }
            });
        }

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        final DetailPageAdapter adapter = new DetailPageAdapter(this);
        adapter.addFragment(infor,"Information");
        adapter.addFragment(image,"Image");
        adapter.addFragment(applicant,"Applicant");
        viewPager.setAdapter(adapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(adapter.getTitle(position));
            }
        });
        mediator.attach();

        navController = Navigation.findNavController(view);
        final ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.navigation_home);
            }
        });

        final ImageButton btnEdit = view.findViewById(R.id.btnEditRequest);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

