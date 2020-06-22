package com.quang.daapp.ui.expertReg;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ui.viewAdapter.MajorSelectAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterExpertFragment extends Fragment {

    private RegisterExpertViewModel viewModel;
    private List<Major> majorsResult;
    private RecyclerView recyclerMajor;
    private MajorSelectAdapter adapter;

    public RegisterExpertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_expert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this)
                .get(RegisterExpertViewModel.class);

        final EditText edtEmail = view.findViewById(R.id.edtUsername);
        final EditText edtPassword = view.findViewById(R.id.edtPassword);
        final EditText edtConfirm = view.findViewById(R.id.edtConfirm);
        final EditText edtFirstName = view.findViewById(R.id.edtFullName);
        final EditText edtFee = view.findViewById(R.id.edtFee);
        final EditText edtBankName = view.findViewById(R.id.edtBankName);
        final EditText edtAccountNo = view.findViewById(R.id.edtAccountNo);
        final EditText edtDescription = view.findViewById(R.id.edtDescription);
        final TextView txtMajorError = view.findViewById(R.id.txtMajorError);
        recyclerMajor = view.findViewById(R.id.recycle_major);

        viewModel.getAllMajor();
        viewModel.getAllMajorResult().observe(getViewLifecycleOwner(), majors -> {
            if(majors == null) return;
            majorsResult = majors;
            adapter = new MajorSelectAdapter(getContext(),majors,new ArrayList<>());
            recyclerMajor.setAdapter(adapter);
            recyclerMajor.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        });

        final NavController navController = Navigation.findNavController(view);

        //Button Back
        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        view.findViewById(R.id.btnExpertRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterExpertFormState state = viewModel.validateDate(edtEmail.getText().toString(),edtPassword.getText().toString(),
                        edtConfirm.getText().toString(),edtFirstName.getText().toString(),edtFee.getText().toString(),
                        edtBankName.getText().toString(),edtAccountNo.getText().toString(),adapter.getSelected().size());

                if(!state.isDataValid()) {
                    MessageDialogFragment mesDialog = new MessageDialogFragment(getString(R.string.mes_error_validate), R.color.colorDanger, R.drawable.ic_error);
                    mesDialog.show(getParentFragmentManager(),getTag());
                    if(state.getUsernameError() != null) edtEmail.setError(getString(state.getUsernameError()));
                    if(state.getFullNameError() != null) edtFirstName.setError(getString(state.getFullNameError()));
                    if(state.getPasswordError() != null) edtPassword.setError(getString(state.getPasswordError()));
                    if(state.getPasswordConfirmError() != null) edtConfirm.setError(getString(state.getPasswordConfirmError()));
                    if(state.getFeeError() != null) edtFee.setError(getString(state.getFeeError()));
                    if(state.getBankAccount() != null) edtBankName.setError(getString(state.getBankAccount()));
                    if(state.getAccountNo() != null) edtAccountNo.setError(getString(state.getAccountNo()));
                    if(state.getMajorError() != null) txtMajorError.setText(getString(state.getMajorError()));
                    return;
                }

                Expert expert = new Expert();
                expert.setEmail(edtEmail.getText().toString());
                expert.setFullName(edtFirstName.getText().toString());
                expert.setPassword(edtPassword.getText().toString());
                expert.setFeePerHour(Float.parseFloat(edtFee.getText().toString()));

                expert.setMajor(adapter.getSelected());
                expert.setBankName(edtBankName.getText().toString());
                expert.setBankAccountNo(edtAccountNo.getText().toString());
                expert.setDescription(edtDescription.getText().toString());

                final LoaderDialogFragment loaderDialog = new LoaderDialogFragment();
                loaderDialog.show(getParentFragmentManager(),loaderDialog.getTag());
                viewModel.register(expert);
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


            }
        });


    }
}
