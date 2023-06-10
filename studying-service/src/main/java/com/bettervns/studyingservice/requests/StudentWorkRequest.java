package com.bettervns.studyingservice.requests;

public record StudentWorkRequest(
        String courseId,
        String groupId,
        String studentId,
        String description,
        String workName
) {
}