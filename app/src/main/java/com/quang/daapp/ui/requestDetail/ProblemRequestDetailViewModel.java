package com.quang.daapp.ui.requestDetail;

import com.quang.daapp.data.model.Expert;
import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.data.repository.ProblemRequestRepository;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProblemRequestDetailViewModel extends ViewModel {

    private MutableLiveData<ProblemRequestDetail> detailLive;
    private MutableLiveData<Number> applyResult = new MutableLiveData<>();
    private MutableLiveData<List<Expert>> applicantResult = new MutableLiveData<>();
    private MutableLiveData<Number> acceptExpertResult = new MutableLiveData<>();
    private ProblemRequestRepository repository;

    public ProblemRequestDetailViewModel() {
        this.repository = ProblemRequestRepository.getInstance();
    }

    LiveData<ProblemRequestDetail> getDetailLive () {return detailLive;}

    LiveData<Number> getApplyResult() {return applyResult;}

    LiveData<List<Expert>> getApplicantResult() {return applicantResult;}

    LiveData<Number> getAcceptExpertResult() {return acceptExpertResult;}

    public void getProblemProquestDetail(int id) {
        detailLive = repository.getRequestDetailById(id);
    }

    public void getApplicantOfRequest(int requestId) {
        applicantResult = repository.getApplicant(requestId);
    }

    public void applyResult(int requestId) {
        applyResult = repository.expertApply(requestId);
    }

    public void acceptExpert(int requestId, int expertId) {
        acceptExpertResult = repository.acceptExpert(requestId,expertId);
    }



}
