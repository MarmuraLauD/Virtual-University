package com.bettervns.adminservice.requests;

public record NewTeacherRequest(
    String name,
    String surname,
    String father,
    String departmentId,
    String email,
    String phone
) {
}