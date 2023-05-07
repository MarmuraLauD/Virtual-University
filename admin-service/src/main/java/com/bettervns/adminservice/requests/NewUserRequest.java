package com.bettervns.adminservice.requests;

public record NewUserRequest(
        String id,
        String name,
        String surname,
        String father,
        String date,
        String email
) {
}