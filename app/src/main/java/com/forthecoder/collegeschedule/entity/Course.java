package com.forthecoder.collegeschedule.entity;

import java.util.Date;

public class Course extends BaseEntity {

    private Long termId = 0L;

    private String title;

    private Date startDate;

    private Date anticipatedEndDate;

    //(in progress, completed, dropped, plan to take)
    private String status;

    private String notes;

    public Course() {

    }

    public Course(Long termId, String title, Date startDate, Date anticipatedEndDate, String status, String notes) {
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.anticipatedEndDate = anticipatedEndDate;
        this.status = status;
        this.notes = notes;
    }

    public Long getTermId() {
        return termId;
    }

    public void setTermId(Long termId) {
        this.termId = termId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getAnticipatedEndDate() {
        return anticipatedEndDate;
    }

    public void setAnticipatedEndDate(Date anticipatedEndDate) {
        this.anticipatedEndDate = anticipatedEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
