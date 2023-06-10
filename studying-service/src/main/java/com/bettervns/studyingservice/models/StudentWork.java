package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "student_work")
@NoArgsConstructor
public class StudentWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "mark", nullable = true)
    private int mark;
    @Column(name = "student_course_group_id", nullable = false)
    private int studentToCourseGroupId;
    @Column(name="filename", nullable = true)
    private String fileName;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getStudentToCourseGroupId() {
        return studentToCourseGroupId;
    }

    public void setStudentToCourseGroupId(int studentToCourseGroupId) {
        this.studentToCourseGroupId = studentToCourseGroupId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String filename) {
        this.fileName = filename;
    }

    public StudentWork(String name, String description, int studentToCourseGroupId) {
        this.name = name;
        this.description = description;
        this.studentToCourseGroupId = studentToCourseGroupId;
    }

    public StudentWork(String name, String description, int mark, int studentToCourseGroupId, String fileName) {
        this.name = name;
        this.description = description;
        this.mark = mark;
        this.studentToCourseGroupId = studentToCourseGroupId;
        this.fileName = fileName;
    }
}