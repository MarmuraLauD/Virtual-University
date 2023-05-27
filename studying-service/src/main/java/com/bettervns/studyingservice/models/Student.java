package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "father", nullable = false)
    private String father;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(String name, String surname, String father, Date date, String email, Group group) {
        this.name = name;
        this.surname = surname;
        this.father = father;
        this.date = date;
        this.email = email;
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", father_name='" + father + '\'' +
                ", admissionDate=" + date +
                ", email='" + email + '\'' +
                ", group_id=" + group.getId() + '\'' +
                '}';
    }
}