package com.bettervns.adminservice.requests;

public record AppointmentRequest(
        String beginTime,
        String endTime,
        String courseId,
        String teacherId,
        String date,
        String meetingLink
) {
}