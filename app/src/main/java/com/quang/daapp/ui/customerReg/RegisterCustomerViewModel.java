package com.quang.daapp.ui.customerReg;


import android.util.Log;
import android.util.Patterns;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.repository.AccountRepository;

import java.sql.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterCustomerViewModel extends ViewModel {
    private MutableLiveData<RegisterCustomerFormState> registerCustomerFormState = new MutableLiveData<>();
    private AccountRepository accountRepository;

    public  RegisterCustomerViewModel() {
        this.accountRepository = AccountRepository.getInstance();
    }

    LiveData<RegisterCustomerFormState> getRegisterCustomerFormState() {
        return registerCustomerFormState;
    }

    public void dataChange(String username, String password, String confirm, String firstName, String lastName, String address, String city, String dob, String primaryLanguage) {
        RegisterCustomerFormState newState = new RegisterCustomerFormState();
        if (!isUserNameValid(username)) {
            newState.setUsernameError(R.string.invalid_username);
            registerCustomerFormState.setValue(newState);
        } else if (!isPasswordValid(password)) {

            newState.setPasswordError(R.string.invalid_password);
            registerCustomerFormState.setValue(newState);
        } else if(!isConfirmValid(password,confirm)) {

            newState.setPasswordConfirmError(R.string.invalid_confirm);
            registerCustomerFormState.setValue(newState);
        } else if(firstName.trim().isEmpty()) {

            newState.setFirstnameError(R.string.invalid_firstname);
            registerCustomerFormState.setValue(newState);
        } else if(lastName.trim().isEmpty()) {

            newState.setLastnameError(R.string.invalid_lastname);
            registerCustomerFormState.setValue(newState);
        } else if(address.trim().isEmpty()) {

            newState.setAddressError(R.string.invalid_address);
            registerCustomerFormState.setValue(newState);
        } else if(city.trim().isEmpty()) {

            newState.setCityError(R.string.invalid_city);
            registerCustomerFormState.setValue(newState);
        } else if(dob.trim().isEmpty()) {

            newState.setDobError(R.string.invalid_dob);
            registerCustomerFormState.setValue(newState);
        } else if(primaryLanguage.trim().isEmpty()) {

            newState.setPrimaryLanguageError(R.string.invalid_primary_language);
            registerCustomerFormState.setValue(newState);
        } else {
            newState.setDataValid(true);
            registerCustomerFormState.setValue(newState);
        }

    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isConfirmValid(String password, String confirm) {
        return password.equals(confirm);
    }

    public void register(String username, String password, String confirm, String firstName, String lastName, String address, String city, String dob, String primaryLanguage) {

        Customer c = new Customer(username,password,firstName,lastName,address,city,dob,primaryLanguage);
        accountRepository.registerCustomer(c);
    }

}
