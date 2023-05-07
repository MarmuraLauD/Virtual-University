package com.bettervns.adminservice.models;

public class Admin {
    private int id;
    private String name;
    private String surname;
    private String father_name;
    private String email;

    public Admin() {
    }

    public Admin(int id, String name, String surname, String father_name, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.father_name = father_name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFather_name() {
        return father_name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", father_name='" + father_name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

