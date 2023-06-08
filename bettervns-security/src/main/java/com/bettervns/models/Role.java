package com.bettervns.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
    private ERole role;

    public Role(ERole role) {
        this.role = role;
    }
    @Override
    public String getAuthority() {
        return null;
    }
}
