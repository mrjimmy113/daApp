package com.quang.daapp.ui.requestDetail;

import com.quang.daapp.data.model.ProblemRequestDetail;
import com.quang.daapp.data.repository.ProblemRequestRepository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProblemRequestDetailViewModel extends ViewModel {

    private MutableLiveData<ProblemRequestDetail> detailLive;
    private ProblemRequestRepository repository;

    public ProblemRequestDetailViewModel() {
        this.repository = ProblemRequestRepository.getInstance();
    }

    LiveData<ProblemRequestDetail> getDetailLive () {return detailLive;}

    public void getProblemProquestDetail(int id) {
        detailLive = repository.getRequestDetailById(id);
    }





}
