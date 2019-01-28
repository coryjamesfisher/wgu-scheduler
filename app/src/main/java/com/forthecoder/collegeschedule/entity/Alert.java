package com.forthecoder.collegeschedule.entity;

import java.util.Date;

public class Alert extends BaseEntity {

    private Integer courseId;
    private Integer assessmentId;
    private Date date;
    private String text;

    public Alert() {

    }

    public Alert(Integer courseId, Integer assessmentId, Date date, String text) {
        this.courseId = courseId;
        this.assessmentId = assessmentId;
        this.date = date;
        this.text = text;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
