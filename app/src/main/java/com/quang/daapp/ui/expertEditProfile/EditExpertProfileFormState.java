package com.quang.daapp.ui.expertEditProfile;

public class EditExpertProfileFormState {
    private Integer fullNameError;

    private Integer feeError;

    private Integer bankAccount;

    private Integer accountNo;

    private boolean isDataValid;

    public Integer getFullNameError() {
        return fullNameError;
    }

    public void setFullNameError(Integer fullNameError) {
        this.fullNameError = fullNameError;
    }

    public Integer getFeeError() {
        return feeError;
    }

    public void setFeeError(Integer feeError) {
        this.feeError = feeError;
    }

    public Integer getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(Integer bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Integer accountNo) {
        this.accountNo = accountNo;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }
}
