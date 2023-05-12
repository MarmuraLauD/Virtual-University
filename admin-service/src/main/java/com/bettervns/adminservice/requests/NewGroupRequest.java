package com.bettervns.adminservice.requests;

public record NewGroupRequest(
        String name,
        String studyingYear,
        String departmentId
) {
}