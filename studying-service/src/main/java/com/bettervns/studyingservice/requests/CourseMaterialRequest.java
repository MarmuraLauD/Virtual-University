package com.bettervns.studyingservice.requests;

public record CourseMaterialRequest(
        String courseId,
        String groupId,
        String description,
        String materialName
) {
}