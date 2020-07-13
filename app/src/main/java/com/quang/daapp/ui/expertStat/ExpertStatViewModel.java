package com.quang.daapp.ui.expertStat;

import com.quang.daapp.data.model.ExpertStat;
import com.quang.daapp.data.repository.ProblemRequestRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExpertStatViewModel extends ViewModel {
    private MutableLiveData<ExpertStat> expertStatMutableLiveData;
    private MutableLiveData<Number> applyResult = new MutableLiveData<>();

    private ProblemRequestRepository problemRequestRepository;

    public ExpertStatViewModel() {
        problemRequestRepository = ProblemRequestRepository.getInstance();
    }

    void getExpertStat(int expertId) {
        expertStatMutableLiveData =  problemRequestRepository.getExpertStat(expertId);
    }

    LiveData<ExpertStat> getExpertStatResult() {
        return expertStatMutableLiveData;
    }

    void  acceptExpert(int requestId, int expertId) {
        applyResult = problemRequestRepository.acceptExpert(requestId, expertId);
    }

    LiveData<Number> getApplyResult() {
        return  applyResult;
    }
}
