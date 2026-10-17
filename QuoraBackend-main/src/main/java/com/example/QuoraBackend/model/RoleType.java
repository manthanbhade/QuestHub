package com.example.quorabackend.security;

public enum RoleType {

    ROLE_USER,
    ROLE_ADMIN;

    public String authority() {
        return this.name();
    }
}
