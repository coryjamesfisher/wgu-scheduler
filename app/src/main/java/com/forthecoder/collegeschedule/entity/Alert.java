package com.forthecoder.collegeschedule.entity;

import java.util.Date;

public class Alert extends BaseEntity {

    private Long termId = 0L;
    private Long courseId = 0L;
    private Long assessmentId = 0L;
    private Date date;
    private String text;
    private ALERT_TYPE type;

    public enum ALERT_TYPE {START, END}

    public Alert() {

    }

    public Alert(Long termId, Long courseId, Long assessmentId, Date date, ALERT_TYPE type, String text) {
        this.termId = termId;
        this.courseId = courseId;
        this.assessmentId = assessmentId;
        this.date = date;
        this.type = type;
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

    public Long getTermId() {
        return termId;
    }

    public void setTermId(Long termId) {
        this.termId = termId;
    }

    public ALERT_TYPE getType() {
        return type;
    }

    public void setType(ALERT_TYPE type) {
        this.type = type;
    }
}
