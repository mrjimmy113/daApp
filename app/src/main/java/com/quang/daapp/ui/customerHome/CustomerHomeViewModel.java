package com.quang.daapp.ui.customerHome;

import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.data.repository.ProblemRequestRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomerHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private MutableLiveData<List<ProblemRequest>> mRequestList;

    private ProblemRequestRepository repository;

    public CustomerHomeViewModel() {
        repository = ProblemRequestRepository.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getCurrentUserRequest() {
        mRequestList =  repository.getCurrentUserRequest();
    }

    public LiveData<List<ProblemRequest>> getRequestList() {
        return mRequestList;
    }
}