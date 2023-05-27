package com.bettervns.studyingservice.requests;

public record DepartmentRequest(
        String name,
        String phone,
        String email
) {
}