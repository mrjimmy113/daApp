package com.quang.daapp.ui.customerProfileEdit;

public class EditCustomerProfileFormState {

    private Integer fullNameError;

    private Integer addressError;

    private boolean isDataValid;

    public Integer getFullNameError() {
        return fullNameError;
    }

    public void setFullNameError(Integer fullNameError) {
        this.fullNameError = fullNameError;
    }

    public Integer getAddressError() {
        return addressError;
    }

    public void setAddressError(Integer addressError) {
        this.addressError = addressError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }
}
