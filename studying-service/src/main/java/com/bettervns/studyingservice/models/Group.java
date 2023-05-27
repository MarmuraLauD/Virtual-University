package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "group_table")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "studying_year", nullable = false)
    private int studyingYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(mappedBy = "registeredGroups")
    private List<Course> courses;

    @ManyToMany(mappedBy = "allowed_groups")
    private List<Appointment> scheduled_appointments;

    public Group(String name, int studyingYear, Department department, List<Course> courses,
                 List<Appointment> appointments) {
        this.name = name;
        this.studyingYear = studyingYear;
        this.department = department;
        this.courses = courses;
        this.scheduled_appointments = appointments;
    }

    public Group(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudyingYear() {
        return studyingYear;
    }

    public void setStudyingYear(int studyingYear) {
        this.studyingYear = studyingYear;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Appointment> getAppointments() {
        return scheduled_appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.scheduled_appointments = appointments;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", studyingYear=" + studyingYear +
                ", department_id=" + department.getId() +
                ", courses=" + courses +
                ", scheduled_appointments=" + scheduled_appointments +
                '}';
    }
}