package com.bettervns.studentsservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    private int group_id;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", father_name='" + father + '\'' +
                ", admissionDate=" + date +
                ", email='" + email + '\'' +
                ", group_id=" + group_id +
                '}';
    }
}