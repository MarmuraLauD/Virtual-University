package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "begin_time", nullable = false)
    private Time beginTime;
    @Column(name = "end_time", nullable = false)
    private Time endTime;
    @Column(name = "date", nullable = false)
    private Date date;
    @Column(name = "meeting_link", nullable = false)
    private String meetingLink;
    @Column(name = "teacher_id", nullable = false)
    private int teacherId;
    @Column(name = "course_id", nullable = false)
    private int courseId;

    public Appointment(Time beginTime, Time endTime, Date date, String meetingLink, int teacherId, int courseId) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.date = date;
        this.meetingLink = meetingLink;
        this.teacherId = teacherId;
        this.courseId = courseId;
    }

    public Time getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Time beginTime) {
        this.beginTime = beginTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", date=" + date +
                ", meetingLink='" + meetingLink + '\'' +
                ", teacherId=" + teacherId +
                ", courseId=" + courseId +
                '}';
    }
}