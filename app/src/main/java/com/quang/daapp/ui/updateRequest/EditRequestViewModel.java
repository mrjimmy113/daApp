package com.quang.daapp.ui.updateRequest;

import com.quang.daapp.R;
import com.quang.daapp.data.model.Major;
import com.quang.daapp.data.repository.MajorRepository;
import com.quang.daapp.data.repository.ProblemRequestRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditRequestViewModel extends ViewModel {

    private MutableLiveData<EditRequestFormState> editRequestFormState = new MutableLiveData<>();
    private MutableLiveData<Number> editRequestResult = new MutableLiveData<>();
    private MutableLiveData<List<Major>> majorResult = new MutableLiveData<>();
    private ProblemRequestRepository repository;
    private MajorRepository majorRepository;

    public EditRequestViewModel() {
        this.repository = ProblemRequestRepository.getInstance();
        this.majorRepository = MajorRepository.getInstance();
    }

    public void editRequest(String[] files,int requestId,Date endDate, String title, String description, int id, ArrayList<String> delImgs) {
        editRequestResult =  repository.updateRequest(files,requestId,endDate,title,description,id,delImgs);
    }

    public void getAllMajor() {
        majorResult = majorRepository.getAllMajor();
    }

    LiveData<List<Major>> getAllMajorResult() {
        return  majorResult;
    }

    LiveData<Number> getEditRequestResult() {return editRequestResult;}

    LiveData<EditRequestFormState> getEditRequestFormState() {
        return editRequestFormState;
    }

    public void onDataChange(String title, String description, Date date, long imgSize) {
        EditRequestFormState formState = new EditRequestFormState();
        if(isEmptyString(title)) {
            formState.setTitleError(R.string.invalid_requried_field);
        }else if(isEmptyString(description)) {
            formState.setDescriptionError(R.string.invalid_requried_field);
        }else if(!isValidEndDate(date)) {
            formState.setEndDateError(R.string.invalid_end_date);
        }else if(imgSize > 5 * 1024 * 1024) {
            formState.setImageError(R.string.invalid_img_over);
        } else {
            formState = new EditRequestFormState(true);
        }
        editRequestFormState.setValue(formState);
    }

    private boolean isEmptyString(String string) {
        return (string == null) || (string.trim().isEmpty());
    }

    boolean isValidEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        int endDayExtra = 2;
        calendar.add(Calendar.DATE, endDayExtra - 1);

        return !date.before(calendar.getTime());
    }

}
