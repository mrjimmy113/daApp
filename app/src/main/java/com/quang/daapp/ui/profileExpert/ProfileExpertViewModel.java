package com.quang.daapp.ui.profileExpert;

import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.ExpertStat;
import com.quang.daapp.data.repository.AccountRepository;
import com.quang.daapp.data.repository.ProblemRequestRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileExpertViewModel extends ViewModel {

    private MutableLiveData<Expert> expertProfileResult;

    private MutableLiveData<ExpertStat> expertStatMutableLiveData;


    private AccountRepository accountRepository;

    private ProblemRequestRepository problemRequestRepository;

    public LiveData<Expert> getProfileResult() {return expertProfileResult;}

    public ProfileExpertViewModel() {
        accountRepository = AccountRepository.getInstance();
        problemRequestRepository = ProblemRequestRepository.getInstance();
    }

    public void getProfile() {
        expertProfileResult = accountRepository.getExpertProfile();
    }

    void getExpertStat(int expertId) {
        expertStatMutableLiveData =  problemRequestRepository.getExpertStat(expertId);
    }

    LiveData<ExpertStat> getExpertStatResult() {
        return expertStatMutableLiveData;
    }




}