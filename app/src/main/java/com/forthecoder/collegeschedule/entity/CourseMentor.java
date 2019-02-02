package com.forthecoder.collegeschedule.entity;

public class CourseMentor extends BaseEntity {
    private Long courseId = 0L;
    private Long mentorId = 0L;
    
    public CourseMentor() {
        
    }
    
    public CourseMentor(Long courseId, Long mentorId) {
        this.courseId = courseId;
        this.mentorId = mentorId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }
}
