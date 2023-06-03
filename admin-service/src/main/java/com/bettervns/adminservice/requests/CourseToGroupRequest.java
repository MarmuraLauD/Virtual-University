package com.bettervns.adminservice.requests;

public record CourseToGroupRequest(
        String courseId,
        String groupId
) {
}