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
import android.widget.ImageButton;

import com.quang.daapp.R;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageButton btnBack = view.findViewById(R.id.btnBack);
        final ImageButton btnChangePassword = view.findViewById(R.id.btnSave);
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
                ChangePasswordFormState state = viewModel.validateData(
                        edtCurrentPassword.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtRetypeNewPassword.getText().toString());

                if(!state.isDataValid()) {
                    MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_validate), R.color.colorDanger,R.drawable.ic_error);
                    mesDialog.show(getParentFragmentManager(),getTag());

                    if(state.getCurrentPasswordError() != null) edtCurrentPassword.setError(getString(state.getCurrentPasswordError()));
                    if(state.getNewPasswordError() != null) edtNewPassword.setError(getString(state.getNewPasswordError()));
                    if(state.getRetypeNewPasswordError() != null) edtRetypeNewPassword.setError(getString(state.getRetypeNewPasswordError()));


                    return;
                }


                final LoaderDialogFragment loader = new LoaderDialogFragment();
                loader.show(getParentFragmentManager(),getTag());

                viewModel.changePasswrod(AuthTokenManager.getToken(getContext()),
                        edtCurrentPassword.getText().toString(),
                        edtNewPassword.getText().toString());

                viewModel.getResultLiveData().observe(getViewLifecycleOwner(), new Observer<Number>() {
                    @Override
                    public void onChanged(Number number) {
                        loader.dismiss();
                        if(number == null) {
                            MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_400), R.color.colorDanger,R.drawable.ic_error);
                            mesDialog.show(getParentFragmentManager(),getTag());
                            return;
                        }
                        int result = number.intValue();
                        if(result == 200) {
                            MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_change_password_success),
                                    R.color.colorSuccess, R.drawable.ic_success,
                                    new MessageDialogFragment.OnMyDialogListener() {
                                        @Override
                                        public void OnOKListener() {
                                            NavController navController = Navigation.findNavController(view);
                                            AuthTokenManager.removeToken(getContext());
                                            navController.navigate(R.id.unAuthActivity);
                                            getActivity().finish();
                                        }
                                    }
                            );
                            mesDialog.show(getParentFragmentManager(),getTag());
                        }else if (result == 202) {
                            MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_change_password_fail), R.color.colorDanger,R.drawable.ic_error);
                            mesDialog.show(getParentFragmentManager(),getTag());
                        }
                    }
                });
            }
        });


    }
}
