package com.bettervns.studyingservice.requests;

public record StudentWorkRequest(
        String courseId,
        String groupId,
        String description,
        String workName
) {
}