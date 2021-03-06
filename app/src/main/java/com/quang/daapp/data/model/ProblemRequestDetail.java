package com.quang.daapp.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProblemRequestDetail {
    private Integer requestId;


    private String title;


    private String description;

    private StatusEnum status;

    private String feedBack;

    private float rating;

    private Date createdDate;

    private Date completedDate;

    private Date deadlineDate;

    private List<String> images = new ArrayList<>();

    private Major major;

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ProblemRequestDetail{" +
                "requestId=" + requestId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", feedBack='" + feedBack + '\'' +
                ", rating=" + rating +
                ", createdDate=" + createdDate +
                ", completedDate=" + completedDate +
                ", deadlineDate=" + deadlineDate +
                ", images=" + images +
                '}';
    }
}
