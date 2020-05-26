package com.quang.daapp.data.model;

public class RegisterModel {
    private String email;

    private String password;

    private String fullName;

    private boolean isExpert;

    public RegisterModel() {
    }

    public RegisterModel(String email, String password, String fullName, boolean isExpert) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.isExpert = isExpert;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isExpert() {
        return isExpert;
    }

    public void setExpert(boolean expert) {
        isExpert = expert;
    }
}
