package com.quang.daapp.ui.customerReg;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.ui.dialog.LoaderDialogFragment;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ultis.DialogManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterCustomerFragment extends Fragment {

    TextView txtEndDate;
    private Date choosenDate;
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
        final EditText edtAddress= view.findViewById(R.id.edtAddress);
        final Spinner spnCity = view.findViewById(R.id.spn_city);
        final Spinner spnPrimaryLanguage = view.findViewById(R.id.spn_primary_language);
        final ImageView btnChooseDate = view.findViewById(R.id.btnChooseDate);
        txtEndDate = view.findViewById(R.id.txtEndDate);
        Calendar cldr = Calendar.getInstance();
        cldr.add(Calendar.YEAR,-18);
        changeDateDisplay(new Date(cldr.getTimeInMillis()));
        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getContext(),
                R.array.city_arrays, android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(adapterCity);

        ArrayAdapter<CharSequence> adapterLanguage = ArrayAdapter.createFromResource(getContext(),
                R.array.language_arrays, android.R.layout.simple_spinner_item);
        adapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPrimaryLanguage.setAdapter(adapterLanguage);

        final  NavController navController = Navigation.findNavController(view);

        //Button Back
        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });


        final ImageButton btnRegister = view.findViewById(R.id.btnCustomerRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterCustomerFormState formState = viewModel.validate(edtEmail.getText().toString(), edtPassword.getText().toString(),
                        edtConfirm.getText().toString(),edtFirstName.getText().toString(), edtAddress.getText().toString());
                if(!formState.isDataValid()) {
                    MessageDialogFragment dialogMes = new MessageDialogFragment(getString(R.string.mes_error_validate),R.color.colorDanger,R.drawable.ic_error);
                    dialogMes.show(getParentFragmentManager(),getTag());
                    if(formState.getUsernameError() != null) edtEmail.setError(getString(formState.getUsernameError()));
                    if(formState.getFullNameError() != null) edtFirstName.setError(getString(formState.getFullNameError()));
                    if(formState.getPasswordError() != null) edtPassword.setError(getString(formState.getPasswordError()));
                    if(formState.getPasswordConfirmError() != null) edtPassword.setError(getString(formState.getPasswordConfirmError()));
                    if(formState.getAddressError() != null) edtAddress.setError(getString(formState.getAddressError()));
                    return;
                }

                Customer customer = new Customer();
                customer.setEmail(edtEmail.getText().toString().trim());
                customer.setFullName(edtFirstName.getText().toString().trim());
                customer.setAddress(edtAddress.getText().toString().trim());
                customer.setPrimaryLanguage(spnPrimaryLanguage.getSelectedItem().toString().trim());
                customer.setCity(spnCity.getSelectedItem().toString().trim());
                customer.setPassword(edtPassword.getText().toString().trim());
                customer.setDob(choosenDate);

                viewModel.register(customer);

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

    private void openDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        cldr.setTime(choosenDate);
        DatePickerDialog picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int ageRange = (Calendar.getInstance().get(Calendar.YEAR)) - year;
                        Log.e("CLMN",ageRange + " - " + year + " - " + (Calendar.getInstance().get(Calendar.YEAR)));
                        if(ageRange >= 18) {
                            cldr.set(Calendar.YEAR, year);
                            cldr.set(Calendar.MONTH, month);
                            cldr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date d = new Date(cldr.getTimeInMillis());
                            changeDateDisplay(d);
                        }else {
                            MessageDialogFragment messageDialogFragment = new MessageDialogFragment("You must be at least 18", R.color.colorWarning,R.drawable.ic_warning);
                            DialogManager.getInstance().showDialog(messageDialogFragment,false);
                        }

                    }
                }, cldr.get(Calendar.YEAR), cldr.get(Calendar.MONTH), cldr.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    private void changeDateDisplay(Date date) {
        choosenDate = date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String a = format.format(choosenDate);
        txtEndDate.setText(a);
    }
}
