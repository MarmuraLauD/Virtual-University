package com.bettervns.adminservice.requests;

public record DepartmentRequest(
        String name,
        String phone,
        String email
) {
}
