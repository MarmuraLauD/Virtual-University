package com.bettervns.adminservice.requests;

public record StudentRequest(
        String name,
        String surname,
        String father,
        String date,
        String email,
        int group
) {
}