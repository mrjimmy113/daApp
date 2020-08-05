package com.quang.daapp.ui.expertReg;

import android.text.TextUtils;
import android.util.Patterns;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.repository.AccountRepository;
import com.quang.daapp.data.repository.MajorRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterExpertViewModel extends ViewModel {

    private MajorRepository majorRepository;

    private AccountRepository accountRepository;

    private MutableLiveData<Number> registerResult;

    private MutableLiveData<List<Major>> allMajorResult;

    public LiveData<Number> getRegisterResult() {
        return  registerResult;
    };

    public LiveData<List<Major>> getAllMajorResult () {
        return  allMajorResult;
    };

    public RegisterExpertViewModel() {
        majorRepository = MajorRepository.getInstance();
        accountRepository = AccountRepository.getInstance();
    }

    public void getAllMajor() {
        allMajorResult = majorRepository.getAllMajor();
    }

    public void register(Expert exp) {
        registerResult = accountRepository.registerExpert(exp);
    }



    public RegisterExpertFormState validateDate(String email, String password,
                                                  String confirm, String fullName, String fee, String bankName, String accountNo, int numMajor) {
        RegisterExpertFormState newState = new RegisterExpertFormState();
        boolean isValid = true;
        if (!isUserNameValid(email)) {
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
        if(!isNumber(fee)) {
            newState.setFeeError(R.string.invalid_fee);
            isValid = false;
        }
        if(bankName.trim().isEmpty()) {
            newState.setBankAccount(R.string.invalid_bank_name);
            isValid = false;
        }
        if(accountNo.trim().isEmpty()) {
            newState.setAccountNo(R.string.invalid_account_no);
            isValid = false;
        }
        if(numMajor == 0) {
            newState.setMajorError(R.string.major_invalid);
            isValid = false;
        }
        newState.setDataValid(isValid);

        return newState;
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isConfirmValid(String password, String confirm) {
        return password.equals(confirm);
    }

    private boolean isNumber(String number) {
        if(number == null || number.trim().isEmpty()) return false;
        return TextUtils.isDigitsOnly(number);
    }
}
