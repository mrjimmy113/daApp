package com.quang.daapp.ui.forgetPassword;

public class ForgetPasswordFormState {

    private Integer emailError;

    private boolean isDataValid;

    public ForgetPasswordFormState() {
    }

    public Integer getEmailError() {
        return emailError;
    }

    public void setEmailError(Integer emailError) {
        this.emailError = emailError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }
}
