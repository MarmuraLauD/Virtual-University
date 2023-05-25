package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.sql.Time;
import java.sql.Date;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
            name = "appointment_group",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> allowed_groups;

    public Appointment(Time beginTime, Time endTime, Date date, String meetingLink, Teacher teacher, Course course,
                       List<Group> allowed_groups) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.date = date;
        this.meetingLink = meetingLink;
        this.teacher = teacher;
        this.course = course;
        this.allowed_groups = allowed_groups;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Group> getAllowed_groups() {
        return allowed_groups;
    }

    public void setAllowed_groups(List<Group> allowed_groups) {
        this.allowed_groups = allowed_groups;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", begin_time=" + beginTime +
                ", end_time=" + endTime +
                ", date=" + date +
                ", meeting_link='" + meetingLink + '\'' +
                ", teacher_id=" + teacher.getId() +
                ", course_id=" + course.getId() +
                ", allowed_groups=" + allowed_groups +
                '}';
    }
}