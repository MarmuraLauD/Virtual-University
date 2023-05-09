package com.bettervns.studentsservice.requests;

public record NewUserRequest(
        String name,
        String surname,
        String father,
        String date,
        String email,
        String groupId
) {
}