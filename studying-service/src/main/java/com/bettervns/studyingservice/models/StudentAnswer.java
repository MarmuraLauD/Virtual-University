package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Data
@Entity
@Table(name = "student_answer")
@NoArgsConstructor
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "mark", nullable = false)
    private int mark = -1;
    @Column(name = "student_id", nullable = false)
    private int studentId;
    @Column(name = "student_work_id", nullable = false)
    private int studentWorkId;

    public StudentAnswer(String fileName, int studentId, int studentWorkId) {
        this.fileName = fileName;
        this.studentId = studentId;
        this.studentWorkId = studentWorkId;
    }

    @Override
    public String toString() {
        return "StudentAnswer{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", mark=" + mark +
                ", studentId=" + studentId +
                ", studentWorkId=" + studentWorkId +
                '}';
    }
}