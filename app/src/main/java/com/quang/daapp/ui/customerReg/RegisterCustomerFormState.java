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
    private Integer firstnameError;
    @Nullable
    private Integer lastnameError;
    @Nullable
    private Integer addressError;
    @Nullable
    private Integer cityError;
    @Nullable
    private Integer dobError;
    @Nullable
    private Integer primaryLanguageError;

    private boolean isDataValid;


    public RegisterCustomerFormState() {
    }

    RegisterCustomerFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.passwordConfirmError = null;
        this.firstnameError = null;
        this.lastnameError = null;
        this.addressError = null;
        this.cityError = null;
        this.dobError = null;
        this.primaryLanguageError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getPasswordConfirmError() {
        return passwordConfirmError;
    }

    @Nullable
    Integer getFirstnameError() {
        return firstnameError;
    }

    @Nullable
    Integer getLastnameError() {
        return lastnameError;
    }

    @Nullable
    Integer getAddressError() {
        return addressError;
    }

    @Nullable
    Integer getCityError() {
        return cityError;
    }

    @Nullable
    Integer getDobError() {
        return dobError;
    }

    @Nullable
    Integer getPrimaryLanguageError() {
        return primaryLanguageError;
    }

    public void setUsernameError(@Nullable Integer usernameError) {
        this.usernameError = usernameError;
    }

    public void setPasswordError(@Nullable Integer passwordError) {
        this.passwordError = passwordError;
    }

    public void setPasswordConfirmError(@Nullable Integer passwordConfirmError) {
        this.passwordConfirmError = passwordConfirmError;
    }

    public void setFirstnameError(@Nullable Integer firstnameError) {
        this.firstnameError = firstnameError;
    }

    public void setLastnameError(@Nullable Integer lastnameError) {
        this.lastnameError = lastnameError;
    }

    public void setAddressError(@Nullable Integer addressError) {
        this.addressError = addressError;
    }

    public void setCityError(@Nullable Integer cityError) {
        this.cityError = cityError;
    }

    public void setDobError(@Nullable Integer dobError) {
        this.dobError = dobError;
    }

    public void setPrimaryLanguageError(@Nullable Integer primaryLanguageError) {
        this.primaryLanguageError = primaryLanguageError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }
}
