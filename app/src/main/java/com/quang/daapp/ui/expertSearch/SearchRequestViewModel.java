package com.quang.daapp.ui.expertSearch;

import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.model.ProblemRequest;
import com.quang.daapp.data.repository.MajorRepository;
import com.quang.daapp.data.repository.ProblemRequestRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchRequestViewModel extends ViewModel {

    private  MutableLiveData<List<Major>> allMajorResult = new MutableLiveData<>();
    private  MutableLiveData<List<ProblemRequest>> searchResult = new MutableLiveData<>();
    private MajorRepository majorRepository;
    private ProblemRequestRepository problemRequestRepository;

    public SearchRequestViewModel() {
        majorRepository = MajorRepository.getInstance();
        problemRequestRepository = ProblemRequestRepository.getInstance();
    }

    public void expertSearch(int major, String city, String language, int time) {
        searchResult = problemRequestRepository.expertSearch(major,city,language,time);
    }

    public LiveData<List<ProblemRequest>> getExpertSearchResult() {
        return searchResult;
    }

    public LiveData<List<Major>> getAllMajorResult () {
        return  allMajorResult;
    };

    public void getAllMajor() {
        allMajorResult = majorRepository.getAllMajor();
    }


}