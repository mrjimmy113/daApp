package com.quang.daapp.ui.expertReg;

public class RegisterExpertFormState {
    private Integer usernameError;

    private Integer passwordError;

    private Integer passwordConfirmError;

    private Integer fullNameError;

    private Integer feeError;

    private Integer bankAccount;

    private Integer accountNo;

    private boolean isDataValid;

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
