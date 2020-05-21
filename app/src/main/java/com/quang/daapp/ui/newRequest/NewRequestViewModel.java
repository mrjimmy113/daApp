package com.quang.daapp.ui.newRequest;

import com.quang.daapp.R;

import java.sql.Date;
import java.util.Calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewRequestViewModel extends ViewModel {

    private MutableLiveData<NewRequestFormState> newRequestFormState = new MutableLiveData<>();

    private int endDayExtra = 2;

    LiveData<NewRequestFormState> getNewRequestFormState() {
        return newRequestFormState;
    }

    public void onDataChange(String title, String description, Date date, long imgSize) {
        NewRequestFormState formState = new NewRequestFormState();
        if(isEmptyString(title)) {
            formState.setTitleError(R.string.invalid_requried_field);
        }else if(isEmptyString(description)) {
            formState.setDescriptionError(R.string.invalid_requried_field);
        }else if(!isValidEndDate(date)) {
            formState.setEndDateError(R.string.invalid_end_date);
        }else if(imgSize > 5 * 1024 * 1024) {
            formState.setImageError(R.string.invalid_img_over);
        } else {
            formState = new NewRequestFormState(true);
        }
        newRequestFormState.setValue(formState);
    }

    private boolean isEmptyString(String string) {
        return (string == null) || (string.trim().isEmpty());
    }

    public boolean isValidEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,endDayExtra);
        if(date.before(calendar.getTime())) {
            return false;
        }
        return true;
    }

}
