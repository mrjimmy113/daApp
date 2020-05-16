package com.quang.daapp.data.model;

import java.sql.Date;

public class Customer {

    private String email;

    private String password;

    private String firstname;

    private String lastname;

    private Date createdDate;

    private String address;

    private String city;

    private String dob;

    private String primaryLanguage;

    public Customer() {
    }

    public Customer(String email, String password, String firstname, String lastname, String address, String city, String dob, String primaryLanguage) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;

        this.address = address;
        this.city = city;
        this.dob = dob;
        this.primaryLanguage = primaryLanguage;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", createdDate=" + createdDate +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", dob=" + dob +
                ", primaryLanguage='" + primaryLanguage + '\'' +
                '}';
    }
}
