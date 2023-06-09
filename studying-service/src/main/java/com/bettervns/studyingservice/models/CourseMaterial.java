package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "course_material")
@NoArgsConstructor
public class CourseMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "file_name", nullable = true)
    private String fileName;
    @Column(nullable = false)
    private int courseToGroupId;

    public CourseMaterial(String name, String description, int courseToGroupId) {
        this.name = name;
        this.description = description;
        this.courseToGroupId = courseToGroupId;
    }

    public CourseMaterial(String name, String description, String fileName, int courseToGroupId) {
        this.name = name;
        this.description = description;
        this.fileName = fileName;
        this.courseToGroupId = courseToGroupId;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCourseToGroupId() {
        return courseToGroupId;
    }

    public void setCourseToGroupId(int courseToGroupId) {
        this.courseToGroupId = courseToGroupId;
    }

    @Override
    public String toString() {
        return "CourseAttachedFile{" +
                "id=" + id +
                ", name=" + name +
                ", description='" + description +
                ", courseToGroupId=" + courseToGroupId +
                '}';
    }
}