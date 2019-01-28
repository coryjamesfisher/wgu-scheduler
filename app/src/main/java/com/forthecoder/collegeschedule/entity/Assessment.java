package com.forthecoder.collegeschedule.entity;

import java.util.Date;

public class Assessment extends BaseEntity {
    private Integer courseId;
    private String title;
    private Date goalDate;

    public Assessment() {

    }

    public Assessment(Integer courseId, String title, Date goalDate) {
        this.courseId = courseId;
        this.title = title;
        this.goalDate = goalDate;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
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
}
