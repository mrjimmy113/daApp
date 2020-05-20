package com.quang.daapp.ui.changePassword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.quang.daapp.R;
import com.quang.daapp.ui.profileCustomer.ProfileCustomerViewModel;
import com.quang.daapp.ultis.AuthTokenManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {
    NavController navigation;
    ChangePasswordViewModel viewModel;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(this).get(ChangePasswordViewModel.class);

        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button btnBack = view.findViewById(R.id.btnBack);
        final Button btnChangePassword = view.findViewById(R.id.btnChangePassword);
        final EditText edtCurrentPassword = view.findViewById(R.id.edtCurrentPassword);
        final EditText edtNewPassword = view.findViewById(R.id.edtNewPassword);
        final EditText edtRetypeNewPassword = view.findViewById(R.id.edtRetypeNewPassword);

        navigation = Navigation.findNavController(view);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.popBackStack();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.changePasswrod(AuthTokenManager.getToken(getContext()),
                        edtCurrentPassword.getText().toString(),
                        edtNewPassword.getText().toString());
            }
        });

        viewModel.getFormState().observe(getViewLifecycleOwner(), new Observer<ChangePasswordFormState>() {
            @Override
            public void onChanged(ChangePasswordFormState formState) {
                if(formState == null) return;

                if(formState.getCurrentPasswordError() != null) {
                    edtCurrentPassword.setError(getString(formState.getCurrentPasswordError()));
                }

                if(formState.getNewPasswordError() != null) {
                    edtNewPassword.setError(getString(formState.getNewPasswordError()));
                }

                if(formState.getRetypeNewPasswordError() != null) {
                    edtRetypeNewPassword.setError(getString(formState.getRetypeNewPasswordError()));
                }
                btnChangePassword.setEnabled(false);
                if(formState.isDataValid()) {
                    btnChangePassword.setEnabled(true);
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.dataChange(edtCurrentPassword.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtRetypeNewPassword.getText().toString());
            }
        };
        edtCurrentPassword.addTextChangedListener(afterTextChangedListener);
        edtNewPassword.addTextChangedListener(afterTextChangedListener);
        edtRetypeNewPassword.addTextChangedListener(afterTextChangedListener);
    }
}
