package com.forthecoder.collegeschedule.entity;

import java.util.Date;

public class Alert extends BaseEntity {

    private Long courseId = 0L;
    private Long assessmentId = 0L;
    private Date date;
    private String text;

    public Alert() {

    }

    public Alert(Long courseId, Long assessmentId, Date date, String text) {
        this.courseId = courseId;
        this.assessmentId = assessmentId;
        this.date = date;
        this.text = text;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
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
