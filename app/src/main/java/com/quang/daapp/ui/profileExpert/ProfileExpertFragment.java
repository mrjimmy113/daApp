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
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.service.RetrofitClient;
import com.quang.daapp.databinding.FragmentProfileCustomerBinding;
import com.quang.daapp.ui.profileCustomer.ProfileCustomerViewModel;
import com.quang.daapp.ultis.AuthTokenManager;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ProfileExpertFragment extends Fragment {

    FragmentProfileCustomerBinding binding;
    Expert data;

    private ProfileCustomerViewModel viewModel;
    NavController navigation;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(ProfileCustomerViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_profile_customer, container, false);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_expert,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}
