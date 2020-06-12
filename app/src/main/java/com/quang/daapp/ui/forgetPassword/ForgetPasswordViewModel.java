package com.quang.daapp.ui.forgetPassword;

import android.util.Patterns;

import com.quang.daapp.R;
import com.quang.daapp.data.repository.AccountRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForgetPasswordViewModel extends ViewModel {

    private MutableLiveData<Number> forgetPasswordResult = new MutableLiveData<>();
    private AccountRepository repository;

    public  ForgetPasswordViewModel() {
        repository = AccountRepository.getInstance();
    }


    public void forgetPassword(String email) {
        forgetPasswordResult = repository.forgetPassword(email);
    }

    public LiveData<Number> getForgetPasswordResult() {
        return forgetPasswordResult;
    }

    public ForgetPasswordFormState formState(String email) {
        ForgetPasswordFormState result = new ForgetPasswordFormState();
        boolean isValid = true;
        if(!isValidEmail(email)) {
            result.setEmailError(R.string.invalid_username);
            isValid = false;
        }

        result.setDataValid(isValid);

        return result;
    }

    private boolean isValidEmail(String email) {
        if(email == null) return  false;
        if(email.trim().isEmpty()) return  false;
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



}
