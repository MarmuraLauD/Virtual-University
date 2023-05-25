package com.bettervns.adminservice.requests;

public record TeacherRequest(
    String name,
    String surname,
    String father,
    String departmentId,
    String email,
    String phone
) {
}