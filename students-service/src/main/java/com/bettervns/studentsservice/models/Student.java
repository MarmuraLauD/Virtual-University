package com.bettervns.studentsservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private int id;
    private String name;
    private String surname;
    private String father;
    private Date date;
    private String email;
    private int groupId;

    public Student(String name, String surname, String father, Date date, String email, int group_id) {
        this.name = name;
        this.surname = surname;
        this.father = father;
        this.date = date;
        this.email = email;
        this.groupId = group_id;
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
                ", group_id=" + groupId +
                '}';
    }
}