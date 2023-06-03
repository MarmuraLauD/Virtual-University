package com.bettervns.adminservice.requests;

public record AppointmentToGroupRequest(
        String appointmentId,
        String groupId
) {
}