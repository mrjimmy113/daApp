package com.quang.daapp.ui.home;

import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.data.model.StatusEnum;
import com.quang.daapp.data.repository.ProblemRequestRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExpertHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private MutableLiveData<List<ProblemRequest>> appliedRequestListResult = new MutableLiveData<>();

    private MutableLiveData<List<ProblemRequest>> processingRequestListResult = new MutableLiveData<>();

    private MutableLiveData<List<ProblemRequest>> tmpCancelRequestList = new MutableLiveData<>();

    private MutableLiveData<List<ProblemRequest>> tmpCompleteRequestList = new MutableLiveData<>();

    private ProblemRequestRepository repository;

    public ExpertHomeViewModel() {
        repository = ProblemRequestRepository.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getCurrentUserAppliedRequest() {
        appliedRequestListResult =  repository.getCurrentUserAppliedRequest();
    }

    public LiveData<List<ProblemRequest>> getAppliedRequestListResult() {
        return appliedRequestListResult;
    }

    public void getCurrentUserProcessingRequest() {
        StatusEnum[] statusEnums = {StatusEnum.ACCEPTED,StatusEnum.PROCESSING};
        processingRequestListResult =  repository.getCurrentUserRequestWithStatus(0,statusEnums);
    }

    public LiveData<List<ProblemRequest>> getProcessingRequestListResult() {
        return processingRequestListResult;
    }

    void getCurrentUserTmpCancelRequest() {
        StatusEnum[] statusEnums = {StatusEnum.TMPCANCEL};
        tmpCancelRequestList =  repository.getCurrentUserRequestWithStatus(0,statusEnums);
    }

    LiveData<List<ProblemRequest>> getTmpCancelRequestList() {
        return tmpCancelRequestList;
    }

    void getCurrentUserTmpCompleteRequest() {
        StatusEnum[] statusEnums = {StatusEnum.TMPCOMPLETE};
        tmpCompleteRequestList =  repository.getCurrentUserRequestWithStatus(0,statusEnums);
    }

    LiveData<List<ProblemRequest>> getTmpCompleteRequestList() {
        return tmpCompleteRequestList;
    }
}