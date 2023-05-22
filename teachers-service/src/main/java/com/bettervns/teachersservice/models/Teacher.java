package com.bettervns.teachersservice.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "teacher_name", nullable = false)
    private String name;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "father_name", nullable = false)
    private String father_name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "chair_id", nullable = false)
    private int chair_id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getChair_id() {
        return chair_id;
    }

    public void setChair_id(int chair_id) {
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

