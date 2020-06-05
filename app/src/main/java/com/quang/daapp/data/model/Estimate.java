package com.quang.daapp.data.model;

public class Estimate {
    private float hour;
    private float total;

    public Estimate(float hour, float total) {
        this.hour = hour;
        this.total = total;
    }

    public float getHour() {
        return hour;
    }
    public void setHour(float hour) {
        this.hour = hour;
    }
    public float getTotal() {
        return total;
    }
    public void setTotal(float total) {
        this.total = total;
    }
}
