package com.bettervns.adminservice.requests;

public record GroupRequest(
        String name,
        String studyingYear,
        String departmentId
) {
}