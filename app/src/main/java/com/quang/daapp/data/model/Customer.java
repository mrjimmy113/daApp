package com.quang.daapp.data.model;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable {

    private String email;

    private String password;

    private String fullName;

    private String imgName;

    private Date createdDate;

    private String address;

    private String city;

    private Date dob;

    private String primaryLanguage;

    public Customer() {
    }


    public Customer(String email, String password, String fullName, String image, Date createdDate, String address, String city, Date dob, String primaryLanguage) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.imgName = image;
        this.createdDate = createdDate;
        this.address = address;
        this.city = city;
        this.dob = dob;
        this.primaryLanguage = primaryLanguage;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getEmail() {
        return email;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
