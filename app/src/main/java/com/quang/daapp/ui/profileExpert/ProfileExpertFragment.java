package com.quang.daapp.ui.profileExpert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.databinding.FragmentProfileExpertBinding;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ultis.AuthTokenManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ProfileExpertFragment extends Fragment {

    FragmentProfileExpertBinding binding;
    Expert data;
    ImageView ivAvatar;

    private ProfileExpertViewModel viewModel;
    NavController navigation;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(ProfileExpertViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_profile_customer, container, false);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_expert,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        final Button btnChangePassword = view.findViewById(R.id.btnChangePassword);
        final TextView txtMajor = view.findViewById(R.id.txtMajor);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        navigation = Navigation.findNavController(view);
        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialogFragment confirmDialog = new ConfirmDialogFragment(
                        getString(R.string.mes_logout_confirm), new ConfirmDialogFragment.OnConfirmDialogListener() {
                    @Override
                    public void OnYesListener() {
                        AuthTokenManager.removeToken(getContext());
                        navigation.navigate(R.id.unAuthActivity2);
                        getActivity().finish();
                    }

                    @Override
                    public void OnNoListener() {

                    }
                }
                );
                confirmDialog.show(getParentFragmentManager(),getTag());
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Gson gson = new GsonBuilder().create();
                bundle.putString("data", gson.toJson(data));
                navigation.navigate(R.id.action_navigation_profile_expert_to_editExpertProfileFragment,bundle);
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.navigate(R.id.action_navigation_profile_expert_to_changePasswordFragment2);
            }
        });

        viewModel.getProfile();
        viewModel.getProfileResult().observe(getViewLifecycleOwner(), new Observer<Expert>() {
            @Override
            public void onChanged(Expert expert) {
                if(expert == null) return;

                binding.setProfile(expert);
                data = expert;
                String strMajor = "";
                for (int i = 0; i< expert.getMajor().size();i++) {
                    strMajor += expert.getMajor().get(i).getMajor();
                    if(i < expert.getMajor().size() - 1) {
                        strMajor += " - ";
                    }
                }
                txtMajor.setText(strMajor);
                Glide.with(view).load(NetworkClient.getImageUrl(data.getImgName())).into(ivAvatar);
            }
        });

    }


}
