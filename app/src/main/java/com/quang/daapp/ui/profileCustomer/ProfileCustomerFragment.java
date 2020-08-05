package com.quang.daapp.ui.profileCustomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.ultis.NetworkClient;
import com.quang.daapp.databinding.FragmentProfileCustomerBinding;
import com.quang.daapp.ui.dialog.ConfirmDialogFragment;
import com.quang.daapp.ultis.AuthTokenManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class ProfileCustomerFragment extends Fragment {

    FragmentProfileCustomerBinding binding;
    Customer data;

    private ProfileCustomerViewModel viewModel;
    NavController navigation;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(ProfileCustomerViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_profile_customer, container, false);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_customer,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        final Button btnChangePassword = view.findViewById(R.id.btnChangePassword);
        final TextView txtDob = view.findViewById(R.id.txtDob);
        final ImageView ivAvatar = view.findViewById(R.id.iv_avatar);

        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialogFragment confirmDialog = new ConfirmDialogFragment(
                        getString(R.string.mes_logout_confirm), new ConfirmDialogFragment.OnConfirmDialogListener() {
                    @Override
                    public void OnYesListener() {
                        AuthTokenManager.removeToken(getContext());
                        navigation.navigate(R.id.unAuthActivity);
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

        navigation = Navigation.findNavController(view);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Gson gson = new GsonBuilder().create();
                bundle.putString("data", gson.toJson(data));
                navigation.navigate(R.id.action_navigation_profile_to_editCustomerProfileFragment,bundle);
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.navigate(R.id.action_navigation_profile_to_changePasswordFragment);
            }
        });

        viewModel.getProfile(AuthTokenManager.getToken(getContext()));
        viewModel.getProfileResult().observe(getViewLifecycleOwner(), new Observer<Customer>() {
            @Override
            public void onChanged(Customer customer) {
                if(customer == null) return;

                binding.setProfile(customer);
                data = customer;
                if(data.getDob() != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    txtDob.setText(format.format(data.getDob()));
                }
                if(data.getImgName() != null && !data.getImgName().trim().isEmpty()) {

                    Picasso.get().load(NetworkClient.getImageUrl(data.getImgName())).into(ivAvatar);
                }




            }
        });

    }


}
