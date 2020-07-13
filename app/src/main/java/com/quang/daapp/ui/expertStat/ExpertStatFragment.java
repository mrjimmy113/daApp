package com.quang.daapp.ui.expertStat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.ExpertStat;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ultis.DialogManager;
import com.quang.daapp.ultis.NetworkClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpertStatFragment extends Fragment {

    private Expert detail;
    private ExpertStat stat;
    private int requestId;
    private ExpertStatViewModel viewModel;


    private ImageView ivAvatar;
    private TextView txtFullname;
    private TextView txtEmail;
    private TextView txtMajor;
    private TextView txtFee;
    private TextView txtCompleted;
    private TextView txtCanceled;
    private RatingBar ratingBar;
    private TextView txtDescription;

    public ExpertStatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ExpertStatViewModel.class);
        return inflater.inflate(R.layout.fragment_expert_stat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        assert getArguments() != null;
        requestId = getArguments().getInt(getString(R.string.key_request_id));
        detail = NetworkClient.getInstance().getGson().fromJson(getArguments().getString("detail"),Expert.class);
        
        ivAvatar = view.findViewById(R.id.iv_avatar);
        ratingBar = view.findViewById(R.id.rb_rating);
        txtFullname = view.findViewById(R.id.txtFullName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtCompleted = view.findViewById(R.id.txtCompleted);
        txtCanceled = view.findViewById(R.id.txtCanceled);
        txtFee = view.findViewById(R.id.txtFee);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtMajor = view.findViewById(R.id.txtMajor);

        Glide.with(this).load(NetworkClient.getImageUrl(detail.getImgName())).into(ivAvatar);
        txtFullname.setText(detail.getFullName());
        txtEmail.setText(detail.getEmail());
        StringBuilder majorText = new StringBuilder();
        for(int i =0 ; i< detail.getMajor().size();i++) {
            majorText.append(detail.getMajor().get(i).getMajor());
            if(i + 1 < detail.getMajor().size()) {
                majorText.append(" - ");
            }
        }
        txtMajor.setText(majorText.toString());
        txtFee.setText(detail.getFeeString());
        txtDescription.setText(detail.getDescription());


        
        viewModel.getExpertStat(detail.getId());
        viewModel.getExpertStatResult().observe(getViewLifecycleOwner(), new Observer<ExpertStat>() {
            @Override
            public void onChanged(ExpertStat expertStat) {
                if(expertStat == null) return;
                stat = expertStat;
                txtCompleted.setText(expertStat.getCompleteCount() +"");
                txtCanceled.setText(expertStat.getCancelCount() +"");
                ratingBar.setRating(expertStat.getRating());
            }
        });

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            navController.popBackStack();
        });

        view.findViewById(R.id.btnApply).setOnClickListener(v -> {
            ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment(getString(R.string.mes_accept_expert_confirm)
                    , new ConfirmDialogFragment.OnConfirmDialogListener() {
                @Override
                public void OnYesListener() {
                    final LoaderDialogFragment loaderDialogFragment = new LoaderDialogFragment();
                    loaderDialogFragment.show(getParentFragmentManager(),getTag());
                    viewModel.acceptExpert(requestId,detail.getId());
                    viewModel.getApplyResult().observe(getViewLifecycleOwner(),number -> {
                        if(number == null) return;
                        loaderDialogFragment.dismiss();
                        int value = number.intValue();
                        if (value == 200) {
                            MessageDialogFragment mesDialog = new MessageDialogFragment(
                                    getString(R.string.mes_accept_expert_success), R.color.colorSuccess, R.drawable.ic_success
                                    , new MessageDialogFragment.OnMyDialogListener() {
                                @Override
                                public void OnOKListener() {
                                    navController.navigate(R.id.navigation_home_customer);
                                }
                            }
                            );
                            mesDialog.show(getParentFragmentManager(), getTag());
                        }
                    });


                }

                @Override
                public void OnNoListener() {

                }
            });

            DialogManager.getInstance().showDialog(dialogFragment,false);
        });
    }
}
