package com.bettervns.teachersservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "father_name", nullable = false)
    private String father_name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "chair_id", nullable = false)
    private int chair_id;

    public Teacher(String name, String surname, String father_name, String email, int chair_id) {
        this.name = name;
        this.surname = surname;
        this.father_name = father_name;
        this.email = email;
        this.chair_id = chair_id;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", father_name='" + father_name + '\'' +
                ", email='" + email + '\'' +
                ", chair_id=" + chair_id +
                '}';
    }
}

