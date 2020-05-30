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
        processingRequestListResult =  repository.getCurrentUserRequestWithStatus(StatusEnum.PROCESSING);
    }

    public LiveData<List<ProblemRequest>> getProcessingRequestListResult() {
        return processingRequestListResult;
    }
}