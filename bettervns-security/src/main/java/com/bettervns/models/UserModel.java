package com.bettervns.models;

public class UserModel {
    private Long id;
    private String email;
    private String password;
    private String role;
    public UserModel(String email, String password, String role) {
        this.role = role;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}