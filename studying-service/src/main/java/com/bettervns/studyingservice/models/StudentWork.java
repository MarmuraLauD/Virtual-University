package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

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
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    @Column(name = "course_group_id", nullable = false)
    private int courseGroupId;

    public StudentWork(String name, String description, int courseGroupId) {
        this.name = name;
        this.description = description;
        this.courseGroupId = courseGroupId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCourseGroupId() {
        return courseGroupId;
    }

    public void setCourseGroupId(int courseGroupId) {
        this.courseGroupId = courseGroupId;
    }

    @Override
    public String toString() {
        return "StudentWork{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", courseGroupId=" + courseGroupId +
                '}';
    }
}