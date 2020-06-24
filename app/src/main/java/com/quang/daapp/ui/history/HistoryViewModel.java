package com.quang.daapp.ui.history;

import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.data.model.StatusEnum;
import com.quang.daapp.data.repository.ProblemRequestRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<ProblemRequest>> completeRequestList = new MutableLiveData<>();

    private MutableLiveData<List<ProblemRequest>> cancelRequestList = new MutableLiveData<>();

    private ProblemRequestRepository repository;

    public HistoryViewModel() {
        repository = ProblemRequestRepository.getInstance();
    }

    void getCompleteRequest(int page) {
        StatusEnum[] statusEnums = {StatusEnum.COMPLETE};
        completeRequestList =  repository.getCurrentUserRequestWithStatus(page,statusEnums);
    }

    void getCancelRequest(int page) {
        StatusEnum[] statusEnums = {StatusEnum.CANCEL};
        cancelRequestList =  repository.getCurrentUserRequestWithStatus(page,statusEnums);
    }

    LiveData<List<ProblemRequest>> getCompleteRequestResult() {
        return  completeRequestList;
    }

    LiveData<List<ProblemRequest>> getCancelRequestResult() {
        return  cancelRequestList;
    }

}
