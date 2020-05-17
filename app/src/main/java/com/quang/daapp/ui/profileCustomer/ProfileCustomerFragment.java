package com.quang.daapp.ui.profileCustomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.ultis.AuthTokenManager;

import org.w3c.dom.Text;

public class ProfileCustomerFragment extends Fragment {

    private ProfileCustomerViewModel viewModel;
    NavController navigation;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(ProfileCustomerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        final Button btnChangePassword = view.findViewById(R.id.btnChangePassword);
        final TextView txtFullName = view.findViewById(R.id.txtFullName);
        final TextView txtCity = view.findViewById(R.id.txtCity);
        final TextView txtEmail = view.findViewById(R.id.txtEmail);
        final TextView txtAddress = view.findViewById(R.id.txtAddress);
        final TextView txtDob = view.findViewById(R.id.txtDob);
        final TextView txtLanguage = view.findViewById(R.id.txtLanguage);

        navigation = Navigation.findNavController(view);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.navigate(R.id.action_navigation_profile_to_editCustomerProfileFragment);
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

                txtFullName.setText(customer.getFirstname() + " " + customer.getLastname());
                txtCity.setText(customer.getCity());


            }
        });

    }


}
