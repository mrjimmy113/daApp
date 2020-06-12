package com.quang.daapp.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quang.daapp.R;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ultis.AuthTokenManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    NavController navController = null;
    private LoginViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        viewModel = ViewModelProviders.of(this)
                .get(LoginViewModel.class);

        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button loginButton = view.findViewById(R.id.btnLogin);
        final Button registerButton = view.findViewById(R.id.btnRegister);



        view.findViewById(R.id.btnForget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_forgetPasswordFragment);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginFormState state =  viewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                if(!state.isDataValid()) {
                    MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_validate),R.color.colorDanger,R.drawable.ic_error);
                    mesDialog.show(getParentFragmentManager(),getTag());
                    if(state.getUsernameError() != null) usernameEditText.setError(getString(state.getUsernameError()));
                    if(state.getPasswordError() != null) passwordEditText.setError(getString(state.getPasswordError()));
                    return;
                }


                viewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                final LoaderDialogFragment loaderDialog = new LoaderDialogFragment();
                loaderDialog.show(getParentFragmentManager(),getTag());

                viewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String token) {
                        loaderDialog.dismiss();
                        if (token == null) {
                            MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_400),R.color.colorDanger,R.drawable.ic_error);
                            mesDialog.show(getParentFragmentManager(),getTag());
                            return;
                        }

                        if(token.trim().length() > 2) {
                            AuthTokenManager.putToken(getContext(),token);
                            navController.navigate(R.id.startingFragment);
                        }else {
                            MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_login_fail),R.color.colorDanger,R.drawable.ic_error);
                            mesDialog.show(getParentFragmentManager(),getTag());
                        }
                    }
                });
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registerChooserFragment);
            }
        });
    }



}
