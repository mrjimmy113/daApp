package com.quang.daapp.ui.updateRequest;

import androidx.annotation.Nullable;

public class EditRequestFormState {

    @Nullable
    private Integer titleError;

    @Nullable
    private Integer descriptionError;

    @Nullable
    private Integer endDateError;

    @Nullable
    private Integer imageError;

    private boolean isDataValid;

    public EditRequestFormState() {
    }

    public EditRequestFormState(boolean isDataValid) {
        titleError = null;
        descriptionError = null;
        endDateError = null;
        imageError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getTitleError() {
        return titleError;
    }

    public void setTitleError(@Nullable Integer titleError) {
        this.titleError = titleError;
    }

    @Nullable
    public Integer getDescriptionError() {
        return descriptionError;
    }

    public void setDescriptionError(@Nullable Integer descriptionError) {
        this.descriptionError = descriptionError;
    }

    @Nullable
    public Integer getEndDateError() {
        return endDateError;
    }

    public void setEndDateError(@Nullable Integer endDateError) {
        this.endDateError = endDateError;
    }

    @Nullable
    public Integer getImageError() {
        return imageError;
    }

    public void setImageError(@Nullable Integer imageError) {
        this.imageError = imageError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }
}
