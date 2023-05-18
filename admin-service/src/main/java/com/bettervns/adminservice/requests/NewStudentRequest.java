package com.bettervns.adminservice.requests;

public record NewStudentRequest(
        String name,
        String surname,
        String father,
        String date,
        String email,
        int group
) {
}