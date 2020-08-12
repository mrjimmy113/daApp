package com.quang.daapp.ui.requestDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ui.dialog.FeedBackDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ultis.WebSocketClient;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFinalInforFragment extends Fragment {

    private ProblemRequestDetailViewModel viewModel;
    private NavController navController;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private boolean isExpert = false;
    private ProblemRequestDetail detail;

    public RequestFinalInforFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this)
                .get(ProblemRequestDetailViewModel.class);
        return inflater.inflate(R.layout.fragment_request_final_infor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int requestId = getArguments().getInt(getString(R.string.key_request_id));
        isExpert = getArguments().getBoolean(getString(R.string.isExpert));
        int mode = getArguments().getInt("mode");

        final RequestDetailInforFragment inform =  new RequestDetailInforFragment();
        final RequestDetailImageFragment image = new RequestDetailImageFragment();
        final RequestDetailMessageFragment message = new RequestDetailMessageFragment(isExpert,requestId,viewModel);
        RequestDetailExpertFragment expertFrag = null;
        RequestDetailCustomerFragment customerFrag = null;



        navController = Navigation.findNavController(view);
        final ImageButton btnBack = view.findViewById(R.id.btnBack);

        if(isExpert) {
            customerFrag = new RequestDetailCustomerFragment();
            viewModel.getCustomerProfile(requestId);
            RequestDetailCustomerFragment finalCusFrag = customerFrag;
            viewModel.getCustomerProfileResult().observe(getViewLifecycleOwner(), new Observer<Customer>() {
                @Override
                public void onChanged(Customer customer) {
                    finalCusFrag.setCustomer(customer);
                }
            });
        }else {
            expertFrag = new RequestDetailExpertFragment();
            viewModel.getExpertProfile(requestId);
            RequestDetailExpertFragment finalExpertFrag = expertFrag;
            viewModel.getExpertProfileResult().observe(getViewLifecycleOwner(), new Observer<Expert>() {
                @Override
                public void onChanged(Expert expert) {
                    finalExpertFrag.setExpert(expert);
                }
            });
        }

        viewModel.getProblemProquestDetail(requestId);
        viewModel.getDetailLive().observe(getViewLifecycleOwner(), new Observer<ProblemRequestDetail>() {
            @Override
            public void onChanged(ProblemRequestDetail problemRequestDetail) {
                if(problemRequestDetail == null) return;
                detail = problemRequestDetail;
                inform.setData(problemRequestDetail);
                image.setDate(problemRequestDetail.getImages());
            }
        });



        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        final DetailPageAdapter adapter = new DetailPageAdapter(this);
        adapter.addFragment(inform,"Information");
        adapter.addFragment(image,"Image");
        if(isExpert) {
            adapter.addFragment(customerFrag,"Partner");
        }else {
            adapter.addFragment(expertFrag,"Partner");
        }
        adapter.addFragment(message,"Message Logs");

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




        if(mode == 1) {

        } else if(mode == 2) {
            WebSocketClient.getInstance().getSubscribeChannelData(requestId).observe(getViewLifecycleOwner(), completeMessageObserver());

        }else if(mode == 3) {
            WebSocketClient.getInstance().getSubscribeChannelData(requestId).observe(getViewLifecycleOwner(), cancelMessageObserver());
        }
    }

    private Observer<ReceiveMessage> completeMessageObserver() {
        return message -> {
            if(message.isExpert() == isExpert) {
                switch (message.getType()) {
                    case COMPLETE_YES: {
                        MessageDialogFragment messageDialog =
                                new MessageDialogFragment(getString(R.string.mes_complete_yes),R.color.colorSuccess,R.drawable.ic_success);
                        messageDialog.show(getParentFragmentManager(),getTag());
                        navController.popBackStack();
                        break;
                    }
                    case NONE: {
                        MessageDialogFragment messageDialog =
                                new MessageDialogFragment("You have already completed this request",R.color.colorWarning,R.drawable.ic_warning);
                        messageDialog.show(getParentFragmentManager(),getTag());
                        break;
                    }
                }
            }
        };
    }

    private Observer<ReceiveMessage> cancelMessageObserver() {
        return message -> {
            switch (message.getType()) {
                case CANCEL_YES: {
                    MessageDialogFragment messageDialog =
                            new MessageDialogFragment(getString(R.string.mes_cancel_yes),R.color.colorSuccess,R.drawable.ic_success);
                    messageDialog.show(getParentFragmentManager(),getTag());
                }
                case NONE: {
                    MessageDialogFragment messageDialog =
                            new MessageDialogFragment("You have already canceled this request",R.color.colorSuccess,R.drawable.ic_success);
                    messageDialog.show(getParentFragmentManager(),getTag());
                }
            }
        };
    }
}
