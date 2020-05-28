package com.quang.daapp.ui.customerReg;

import androidx.annotation.Nullable;

public class RegisterCustomerFormState {

    private Integer usernameError;

    private Integer passwordError;

    private Integer passwordConfirmError;

    private Integer fullNameError;


    private boolean isDataValid;


    private Integer addressError;

    public Integer getUsernameError() {
        return usernameError;
    }

    public void setUsernameError(Integer usernameError) {
        this.usernameError = usernameError;
    }

    public Integer getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(Integer passwordError) {
        this.passwordError = passwordError;
    }

    public Integer getPasswordConfirmError() {
        return passwordConfirmError;
    }

    public void setPasswordConfirmError(Integer passwordConfirmError) {
        this.passwordConfirmError = passwordConfirmError;
    }

    public Integer getFullNameError() {
        return fullNameError;
    }

    public void setFullNameError(Integer fullNameError) {
        this.fullNameError = fullNameError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }

    public Integer getAddressError() {
        return addressError;
    }

    public void setAddressError(Integer addressError) {
        this.addressError = addressError;
    }
}
