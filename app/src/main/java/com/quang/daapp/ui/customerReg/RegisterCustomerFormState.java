package com.quang.daapp.ui.customerReg;

import androidx.annotation.Nullable;

public class RegisterCustomerFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer passwordConfirmError;
    @Nullable
    private Integer fullNameError;


    private boolean isDataValid;


    public RegisterCustomerFormState() {
    }

    RegisterCustomerFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.passwordConfirmError = null;
        this.fullNameError = null;

        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    public void setUsernameError(@Nullable Integer usernameError) {
        this.usernameError = usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(@Nullable Integer passwordError) {
        this.passwordError = passwordError;
    }

    @Nullable
    public Integer getPasswordConfirmError() {
        return passwordConfirmError;
    }

    public void setPasswordConfirmError(@Nullable Integer passwordConfirmError) {
        this.passwordConfirmError = passwordConfirmError;
    }

    @Nullable
    public Integer getFullNameError() {
        return fullNameError;
    }

    public void setFullNameError(@Nullable Integer fullNameError) {
        this.fullNameError = fullNameError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }
}
