package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "Student_Course_Group")
@NoArgsConstructor
public class StudentToCourseGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "student_id", nullable = false)
    private int studentId;
    @Column(name = "course_group_id", nullable = false)
    private int courseGroupId;

    @OneToMany(mappedBy = "studentToCourseGroup", cascade = CascadeType.ALL)
    private List<StudentWork> studentWorks;

    @Override
    public String toString() {
        return "StudentToCourseGroup{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", studentCourseGroupId=" + courseGroupId +
                ", studentWorks=" + studentWorks +
                '}';
    }
}
