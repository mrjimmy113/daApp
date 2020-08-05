package com.quang.daapp.ui.newRequest;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.repository.MajorRepository;
import com.quang.daapp.data.repository.ProblemRequestRepository;

import java.io.File;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.MultipartBody;

public class NewRequestViewModel extends ViewModel {

    private MutableLiveData<NewRequestFormState> newRequestFormState = new MutableLiveData<>();
    private MutableLiveData<Number> newRequestLive = new MutableLiveData<>();
    private MutableLiveData<List<Major>> majorResult = new MutableLiveData<>();
    private ProblemRequestRepository repository;
    private MajorRepository majorRepository;

    public NewRequestViewModel() {
        this.repository = ProblemRequestRepository.getInstance();
        this.majorRepository = MajorRepository.getInstance();
    }

    public void createNewRequest(String[] files,Date endDate, String title, String description, int id) {
        newRequestLive =  repository.createNewRequest(files,endDate,title,description,id);
    }

    public void getAllMajor() {
        majorResult = majorRepository.getAllMajor();
    }

    LiveData<List<Major>> getAllMajorResult() {
        return  majorResult;
    }

    private int endDayExtra = 2;

    LiveData<Number> getNewRequestResult() {return newRequestLive;}

    LiveData<NewRequestFormState> getNewRequestFormState() {
        return newRequestFormState;
    }

    public NewRequestFormState validate(String title, String description, Date date, long imgSize) {
        NewRequestFormState formState = new NewRequestFormState();
        boolean isValid = true;
        if(isEmptyString(title)) {
            formState.setTitleError(R.string.invalid_requried_field);
            isValid = false;
        }
        if(isEmptyString(description)) {
            formState.setDescriptionError(R.string.invalid_requried_field);
            isValid = false;
        }
        if(!isValidEndDate(date)) {
            formState.setEndDateError(R.string.invalid_end_date);
            isValid = false;
        }
        if(imgSize > 5 * 1024 * 1024) {
            formState.setImageError(R.string.invalid_img_over);
            isValid = false;
        }
        formState.setDataValid(isValid);
        return formState;
    }

    private boolean isEmptyString(String string) {
        return (string == null) || (string.trim().isEmpty());
    }

    public boolean isValidEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,endDayExtra - 1);

        if(date.before(calendar.getTime())) {
            return false;
        }
        return true;
    }

}
