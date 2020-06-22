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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Major major1 = (Major) o;
        return id == major1.id &&
                Objects.equals(major, major1.major);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, major);
    }
}
