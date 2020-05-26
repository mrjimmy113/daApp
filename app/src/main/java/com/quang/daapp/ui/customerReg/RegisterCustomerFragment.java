package com.quang.daapp.ui.customerReg;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.quang.daapp.R;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;

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
        final EditText edtFirstName = view.findViewById(R.id.edtFullName);
        final Spinner spnAccountType = view.findViewById(R.id.spn_account_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.account_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAccountType.setAdapter(adapter);

        final ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.loginFragment);
            }
        });

        final Button btnRegister = view.findViewById(R.id.btnCustomerRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterCustomerFormState formState = viewModel.validate(edtEmail.getText().toString(), edtPassword.getText().toString(),
                        edtConfirm.getText().toString(),edtFirstName.getText().toString());
                if(!formState.isDataValid()) {
                    MessageDialogFragment dialogMes = new MessageDialogFragment(getString(R.string.mes_error_validate),R.color.colorDanger,R.drawable.ic_error);
                    dialogMes.show(getParentFragmentManager(),getTag());
                    if(formState.getUsernameError() != null) edtEmail.setError(getString(formState.getUsernameError()));
                    if(formState.getFullNameError() != null) edtFirstName.setError(getString(formState.getFullNameError()));
                    if(formState.getPasswordError() != null) edtPassword.setError(getString(formState.getPasswordError()));
                    if(formState.getPasswordConfirmError() != null) edtPassword.setError(getString(formState.getPasswordConfirmError()));

                    return;
                }


                viewModel.register(edtEmail.getText().toString(), edtPassword.getText().toString(),
                        edtFirstName.getText().toString(), spnAccountType.getSelectedItemPosition() != 0);
                final LoaderDialogFragment loaderDialog = new LoaderDialogFragment();
                loaderDialog.show(getParentFragmentManager(),loaderDialog.getTag());

                if(viewModel.getRegisterResult() != null) {
                    viewModel.getRegisterResult().observe(getViewLifecycleOwner(), new Observer<Number>() {
                        @Override
                        public void onChanged(Number number) {
                            loaderDialog.dismiss();
                            MessageDialogFragment dialogMes = null;
                            if(number == null) {
                                dialogMes = new MessageDialogFragment(getString(R.string.mes_error_400),R.color.colorDanger,R.drawable.ic_error);
                            }else {
                                int result = number.intValue();
                                if(result == 201) {
                                    dialogMes = new MessageDialogFragment(getString(R.string.mes_register_success),
                                            R.color.colorSuccess, R.drawable.ic_success, new MessageDialogFragment.OnMyDialogListener() {
                                        @Override
                                        public void OnOKListener() {
                                            NavController navController = Navigation.findNavController(view);
                                            navController.navigate(R.id.loginFragment);
                                        }
                                    });
                                }else  if(result == 409) {
                                    dialogMes = new MessageDialogFragment(getString(R.string.mes_register_email_exist),R.color.colorDanger,R.drawable.ic_error);
                                }else if (result == 400) {
                                    dialogMes = new MessageDialogFragment(getString(R.string.mes_error_400),R.color.colorDanger,R.drawable.ic_error);
                                }
                            }

                            if(dialogMes != null) dialogMes.show(getParentFragmentManager(),dialogMes.getTag());

                        }
                    });
                }else {
                    MessageDialogFragment dialogMes = new MessageDialogFragment(getString(R.string.mes_error_400),R.color.colorDanger,R.drawable.ic_error);
                    dialogMes.show(getParentFragmentManager(),getTag());
                }
            }
        });

    }
}
