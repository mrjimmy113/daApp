package com.quang.daapp.ui.profileExpert;

import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.repository.AccountRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileExpertViewModel extends ViewModel {

    private MutableLiveData<Expert> expertProfileResult;

    private AccountRepository accountRepository;

    public LiveData<Expert> getProfileResult() {return expertProfileResult;}

    public ProfileExpertViewModel() {
        accountRepository = AccountRepository.getInstance();
    }

    public void getProfile() {
        expertProfileResult = accountRepository.getExpertProfile();
    }




}