package com.quang.daapp.ui.customerReg;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.quang.daapp.R;

import java.sql.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterCustomerFragment extends Fragment {

    private RegisterCustomerViewModel viewModel;

    public RegisterCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_customer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this)
                .get(RegisterCustomerViewModel.class);

        final EditText edtEmail = view.findViewById(R.id.edtUsername);
        final EditText edtPassword = view.findViewById(R.id.edtPassword);
        final EditText edtConfirm = view.findViewById(R.id.edtConfirm);
        final EditText edtFirstName = view.findViewById(R.id.edtFirstName);
        final EditText edtLastName = view.findViewById(R.id.edtLastName);
        final EditText edtAddress = view.findViewById(R.id.edtAddress);
        final EditText edtCity = view.findViewById(R.id.edtCity);
        final EditText edtDob = view.findViewById(R.id.edtDob);
        final EditText edtPrimaryLanguage = view.findViewById(R.id.edtLanguage);
        final Button btnRegister = view.findViewById(R.id.btnCustomerRegister);

        viewModel.getRegisterCustomerFormState().observe(getViewLifecycleOwner(), new Observer<RegisterCustomerFormState>() {
            @Override
            public void onChanged(RegisterCustomerFormState registerCustomerFormState) {
                if(registerCustomerFormState == null) return;

                if(registerCustomerFormState.getUsernameError() != null ){
                    edtEmail.setError(getString(registerCustomerFormState.getUsernameError()));
                }
                if(registerCustomerFormState.getPasswordError() != null ){
                    edtPassword.setError(getString(registerCustomerFormState.getPasswordError()));
                }
                if(registerCustomerFormState.getPasswordConfirmError() != null ) {
                    edtConfirm.setError(getString(registerCustomerFormState.getPasswordConfirmError()));
                }
                if(registerCustomerFormState.getFirstnameError() != null ){
                    edtFirstName.setError(getString(registerCustomerFormState.getFirstnameError()));
                }
                if(registerCustomerFormState.getLastnameError() != null ){
                    edtLastName.setError(getString(registerCustomerFormState.getLastnameError()));
                }
                if(registerCustomerFormState.getAddressError() != null ){
                    edtAddress.setError(getString(registerCustomerFormState.getAddressError()));
                }
                if(registerCustomerFormState.getDobError() != null ){
                    edtDob.setError(getString(registerCustomerFormState.getDobError()));
                }
                if(registerCustomerFormState.getCityError() != null ){
                    edtCity.setError(getString(registerCustomerFormState.getCityError()));
                }
                if(registerCustomerFormState.getPrimaryLanguageError() != null){
                    edtPrimaryLanguage.setError(getString(registerCustomerFormState.getPrimaryLanguageError()));
                }
                btnRegister.setEnabled(false);
                if(registerCustomerFormState.isDataValid()) {
                    btnRegister.setEnabled(true);
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
                viewModel.dataChange(edtEmail.getText().toString(),edtPassword.getText().toString(),
                        edtConfirm.getText().toString(),edtFirstName.getText().toString(),
                        edtLastName.getText().toString(), edtAddress.getText().toString(),
                        edtCity.getText().toString(),edtDob.getText().toString(),
                        edtPrimaryLanguage.getText().toString());
            }
        };

        edtEmail.addTextChangedListener(afterTextChangedListener);
        edtPassword.addTextChangedListener(afterTextChangedListener);
        edtFirstName.addTextChangedListener(afterTextChangedListener);
        edtLastName.addTextChangedListener(afterTextChangedListener);
        edtAddress.addTextChangedListener(afterTextChangedListener);
        edtCity.addTextChangedListener(afterTextChangedListener);
        edtConfirm.addTextChangedListener(afterTextChangedListener);
        edtDob.addTextChangedListener(afterTextChangedListener);
        edtPrimaryLanguage.addTextChangedListener(afterTextChangedListener);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.register(edtEmail.getText().toString(),edtPassword.getText().toString(),
                        edtConfirm.getText().toString(),edtFirstName.getText().toString(),
                        edtLastName.getText().toString(), edtAddress.getText().toString(),
                        edtCity.getText().toString(),edtDob.getText().toString(),
                        edtPrimaryLanguage.getText().toString());
            }
        });

    }
}
