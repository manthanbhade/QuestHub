package com.example.QuoraBackend.security.dto;

// Response returned after successful authentication.
public class AuthResponse {

    private String token;

    public AuthResponse(String token)  {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
