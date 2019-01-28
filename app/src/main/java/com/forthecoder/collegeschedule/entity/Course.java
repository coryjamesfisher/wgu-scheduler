package com.forthecoder.collegeschedule.entity;

import java.util.Date;

public class Course extends BaseEntity {

    private Integer termId;

    private String title;

    private Date startDate;

    private Date anticipatedEndDate;

    //(in progress, completed, dropped, plan to take)
    private String status;

    private Integer mentorId;

    private String notes;

    public Course() {

    }

    public Course(Integer termId, String title, Date startDate, Date anticipatedEndDate, String status, Integer mentorId, String notes) {
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.anticipatedEndDate = anticipatedEndDate;
        this.status = status;
        this.mentorId = mentorId;
        this.notes = notes;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
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

    public Integer getMentorId() {
        return mentorId;
    }

    public void setMentorId(Integer mentorId) {
        this.mentorId = mentorId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
