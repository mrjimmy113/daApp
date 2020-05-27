package com.quang.daapp.ui.changePassword;

import com.quang.daapp.R;
import com.quang.daapp.data.repository.AccountRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChangePasswordViewModel extends ViewModel {

    private MutableLiveData<Number> mutableLiveData = new MutableLiveData<>();


    public LiveData<Number> getResultLiveData() {return mutableLiveData;}


    private AccountRepository accountRepository;

    public  ChangePasswordViewModel() {
        accountRepository = AccountRepository.getInstance();
    }

    public void changePasswrod(String token, String currentPassword, String newPassword) {
        mutableLiveData = accountRepository.changePassword(token,currentPassword,newPassword);
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isConfirmValid(String password, String confirm) {
        return password.equals(confirm);
    }

    private boolean isFieldRequired(String s) {
        return s == null || s.trim().isEmpty();
    }

    public ChangePasswordFormState validateData(String currentPassword, String newPassword, String retypeNewPassword) {
        ChangePasswordFormState state = new ChangePasswordFormState();
        boolean isValid = true;
        if(isFieldRequired(currentPassword)) {
            state.setCurrentPasswordError(R.string.invalid_requried_field);
            isValid = false;
        }
        if(!isPasswordValid(newPassword)) {
            state.setNewPasswordError(R.string.invalid_password);
            isValid = false;
        }
        if(!isConfirmValid(newPassword,retypeNewPassword)) {
            state.setRetypeNewPasswordError(R.string.invalid_confirm);
            isValid = false;
        }
        state.setDataValid(isValid);
        return  state;
    }
}
