package com.example.QuoraBackend.dto.request;

import lombok.Getter;

@Getter

// Carries login credentials from client to backend.
public class LoginRequest {

    private String email;
    private String password;
}
