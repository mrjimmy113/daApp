package com.quang.daapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.quang.daapp.data.repository.AccountRepository;
import com.quang.daapp.R;

import java.io.IOException;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> loginResult = new MutableLiveData<>();
    private AccountRepository accountRepository;

    public LoginViewModel() {
        this.accountRepository = AccountRepository.getInstance();
    }

    LiveData<String> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
            loginResult = accountRepository.login(username,password);
    }


    public LoginFormState loginDataChanged(String username, String password) {
        LoginFormState state = new LoginFormState();
        boolean isValue = true;
        if (!isUserNameValid(username)) {
            state.setUsernameError(R.string.invalid_username);
            isValue = false;
        }
        if (!isPasswordValid(password)) {
            state.setPasswordError(R.string.invalid_password);
            isValue = false;
        }
        state.setDataValid(isValue);
        return  state;
    }

    // A placeholder username validation check
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
        return password != null && password.trim().length() >= 5;
    }
}
