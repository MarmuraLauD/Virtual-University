package com.bettervns.adminservice.models;

public class User {
    private Long id;
    private String email;
    private String password;
    private ERole role;
    public User(String email, String password, ERole role) {
        this.role = role;
        this.email = email;
        this.password = password;
    }
}