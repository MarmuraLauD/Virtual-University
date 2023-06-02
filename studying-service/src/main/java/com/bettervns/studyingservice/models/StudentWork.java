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
    @Column(name = "student_id", nullable = false)
    private int studentId;
    @Column(name = "name", nullable = false)
    private int name;
    @Column(name = "mark", nullable = false)
    private int mark;
    @Column(name = "file_link", nullable = false)
    private String fileLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_course_group_id")
    private StudentToCourseGroup studentToCourseGroup;

    public StudentWork(int studentId, int name, int mark, String fileLink, StudentToCourseGroup studentToCourseGroup) {
        this.studentId = studentId;
        this.name = name;
        this.mark = mark;
        this.fileLink = fileLink;
        this.studentToCourseGroup = studentToCourseGroup;
    }

    @Override
    public String toString() {
        return "StudentWork{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", name=" + name +
                ", mark=" + mark +
                ", fileLink='" + fileLink + '\'' +
                ", studentToCourseGroup id=" + studentToCourseGroup.getId() +
                '}';
    }
}