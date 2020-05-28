package com.quang.daapp.data.model;

import java.util.Objects;

public class Major {

    private int id;

    private String major;

    public Major(int id, String major) {
        this.id = id;
        this.major = major;
    }

    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return this.major;
    }
}
