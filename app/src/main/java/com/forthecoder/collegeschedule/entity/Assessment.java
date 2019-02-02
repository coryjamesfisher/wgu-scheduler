package com.forthecoder.collegeschedule.entity;

import java.util.Date;

public class Assessment extends BaseEntity {
    private Long courseId = 0L;
    private String type;
    private String title;
    private String status;
    private Date goalDate;

    public Assessment() {

    }

    public Assessment(Long courseId, String type, String title, Date goalDate, String status) {
        this.courseId = courseId;
        this.type = type;
        this.title = title;
        this.goalDate = goalDate;
        this.status = status;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(Date goalDate) {
        this.goalDate = goalDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
