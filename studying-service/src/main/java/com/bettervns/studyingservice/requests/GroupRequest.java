package com.bettervns.studyingservice.requests;

public record GroupRequest(
        String name,
        String studyingYear,
        String departmentId
) {
}