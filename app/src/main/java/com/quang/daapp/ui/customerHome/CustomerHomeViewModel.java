package com.quang.daapp.ui.customerHome;

import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.data.model.StatusEnum;
import com.quang.daapp.data.repository.ProblemRequestRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomerHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private MutableLiveData<List<ProblemRequest>> newRequestList = new MutableLiveData<>();

    private MutableLiveData<List<ProblemRequest>> acceptedRequestList = new MutableLiveData<>();

    private ProblemRequestRepository repository;

    public CustomerHomeViewModel() {
        repository = ProblemRequestRepository.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getCurrentUserNewRequest() {
        newRequestList =  repository.getCurrentUserRequestWithStatus(StatusEnum.NEW);
    }

    public LiveData<List<ProblemRequest>> getNewRequestList() {
        return newRequestList;
    }

    public void getCurrentUserAcceptedRequest() {
        acceptedRequestList =  repository.getCurrentUserRequestWithStatus(StatusEnum.ACCEPTED);
    }

    public LiveData<List<ProblemRequest>> getAcceptedRequestList() {
        return acceptedRequestList;
    }
}