package com.bettervns.adminservice.requests;

public record NewDepartmentRequest(
        String name,
        String phone,
        String email
) {
}
