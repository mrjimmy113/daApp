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

    private MutableLiveData<List<ProblemRequest>> tmpCancelRequestList = new MutableLiveData<>();

    private MutableLiveData<List<ProblemRequest>> tmpCompleteRequestList = new MutableLiveData<>();

    private ProblemRequestRepository repository;

    public CustomerHomeViewModel() {
        repository = ProblemRequestRepository.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    void getCurrentUserNewRequest(int page) {
        StatusEnum[] statusEnums = {StatusEnum.NEW};
        newRequestList =  repository.getCurrentUserRequestWithStatus(page,statusEnums);
    }

    LiveData<List<ProblemRequest>> getNewRequestList() {
        return newRequestList;
    }

    void getCurrentUserAcceptedRequest(int page) {
        StatusEnum[] statusEnums = {StatusEnum.ACCEPTED,StatusEnum.PROCESSING};
        acceptedRequestList =  repository.getCurrentUserRequestWithStatus(page,statusEnums);
    }

    LiveData<List<ProblemRequest>> getAcceptedRequestList() {
        return acceptedRequestList;
    }

    void getCurrentUserTmpCancelRequest(int page) {
        StatusEnum[] statusEnums = {StatusEnum.TMPCANCEL};
        tmpCancelRequestList =  repository.getCurrentUserRequestWithStatus(page,statusEnums);
    }

    LiveData<List<ProblemRequest>> getTmpCancelRequestList() {
        return tmpCancelRequestList;
    }

    void getCurrentUserTmpCompleteRequest(int page) {
        StatusEnum[] statusEnums = {StatusEnum.TMPCOMPLETE};
        tmpCompleteRequestList =  repository.getCurrentUserRequestWithStatus(page,statusEnums);
    }

    LiveData<List<ProblemRequest>> getTmpCompleteRequestList() {
        return tmpCompleteRequestList;
    }


}