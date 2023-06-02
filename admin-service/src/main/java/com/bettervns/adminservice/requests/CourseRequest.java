package com.bettervns.adminservice.requests;

public record CourseRequest(
        String name,
        String departmentId,
        String teacherId
) {
}