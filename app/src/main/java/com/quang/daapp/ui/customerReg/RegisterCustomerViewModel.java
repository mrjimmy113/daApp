package com.quang.daapp.ui.customerReg;


import android.util.Patterns;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Customer;
import com.quang.daapp.data.repository.AccountRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterCustomerViewModel extends ViewModel {

    private MutableLiveData<Number> registerResult;

    LiveData<Number> getRegisterResult() {return registerResult;};

    private AccountRepository accountRepository;

    public  RegisterCustomerViewModel() {
        this.accountRepository = AccountRepository.getInstance();
    }


    public RegisterCustomerFormState validate(String username, String password, String confirm, String fullName, String address) {
        RegisterCustomerFormState newState = new RegisterCustomerFormState();
        boolean isValid = true;
        if (!isUserNameValid(username)) {
            newState.setUsernameError(R.string.invalid_username);
            isValid = false;
        }
        if (!isPasswordValid(password)) {
            newState.setPasswordError(R.string.invalid_password);
            isValid = false;
        }
        if(!isConfirmValid(password,confirm)) {
            newState.setPasswordConfirmError(R.string.invalid_confirm);
            isValid = false;
        }
        if(fullName.trim().isEmpty()) {
            newState.setFullNameError(R.string.invalid_fullName);
            isValid = false;
        }
        if(address.trim().isEmpty()) {
            newState.setAddressError(R.string.invalid_address);
            isValid = false;
        }
        newState.setDataValid(isValid);

        return  newState;
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

    public void register(Customer customer) {
        registerResult = accountRepository.registerCustomer(customer);
    }

}
