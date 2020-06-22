package com.quang.daapp.ui.expertEditProfile;

import android.text.TextUtils;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.repository.AccountRepository;
import com.quang.daapp.data.repository.MajorRepository;
import com.quang.daapp.ui.expertReg.RegisterExpertFormState;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditExpertProfileViewModel extends ViewModel {
    private MajorRepository majorRepository;

    private AccountRepository accountRepository;

    private MutableLiveData<List<Major>> allMajorResult = new MutableLiveData<>();

    private MutableLiveData<Number> updateResult = new MutableLiveData<>();

    public LiveData<List<Major>> getAllMajorResult () {
        return  allMajorResult;
    };

    public LiveData<Number> getUpdateResult() {return updateResult;}

    public EditExpertProfileViewModel() {
        majorRepository = MajorRepository.getInstance();
        accountRepository = AccountRepository.getInstance();
    }

    public void updateExpert(String filePath,Expert expert) {
        updateResult = accountRepository.updateExpert(filePath,expert);
    }


    public void getAllMajor() {
        allMajorResult = majorRepository.getAllMajor();
    }

    public EditExpertProfileFormState validateDate(String fullName, String fee, String bankName, String accountNo, int majorCount) {
        EditExpertProfileFormState newState = new EditExpertProfileFormState();
        boolean isValid = true;

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
        if(majorCount <= 0) {
            newState.setMajorError(R.string.major_invalid);
            isValid = false;
        }

        newState.setDataValid(isValid);

        return newState;
    }

    private boolean isNumber(String number) {
        return TextUtils.isDigitsOnly(number);
    }
}
