package com.quang.daapp.ui.changePassword;

import androidx.annotation.Nullable;

public class ChangePasswordFormState {

    @Nullable
    private Integer currentPasswordError;

    @Nullable
    private Integer newPasswordError;

    @Nullable
    private Integer retypeNewPasswordError;

    private boolean isDataValid;

    public ChangePasswordFormState() {
    }


    @Nullable
    public Integer getNewPasswordError() {
        return newPasswordError;
    }

    public void setNewPasswordError(@Nullable Integer newPasswordError) {
        this.newPasswordError = newPasswordError;
    }

    @Nullable
    public Integer getRetypeNewPasswordError() {
        return retypeNewPasswordError;
    }

    public void setRetypeNewPasswordError(@Nullable Integer retypeNewPasswordError) {
        this.retypeNewPasswordError = retypeNewPasswordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }

    @Nullable
    public Integer getCurrentPasswordError() {
        return currentPasswordError;
    }

    public void setCurrentPasswordError(@Nullable Integer currentPasswordError) {
        this.currentPasswordError = currentPasswordError;
    }
}
