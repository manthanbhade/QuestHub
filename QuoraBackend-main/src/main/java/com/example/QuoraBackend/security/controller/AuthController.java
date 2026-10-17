package com.example.QuoraBackend.security.controller;

import com.example.QuoraBackend.dto.request.LoginRequest;
import com.example.QuoraBackend.dto.request.RegisterRequest;
import com.example.QuoraBackend.dto.response.LoginResponse;
import com.example.QuoraBackend.dto.response.RegisterResponse;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.repository.RoleRepository;
import com.example.QuoraBackend.repository.UserRepository;
import com.example.QuoraBackend.security.dto.AuthResponse;
import com.example.QuoraBackend.security.service.AuthService;
import com.example.QuoraBackend.security.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    // login user
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        String token = authService.login(request);

        return ResponseEntity.ok(
                new LoginResponse(
                        "Welcome back! You’re successfully logged in ",
                        token
                )
        );
    }

    // returning response in the form of the dto
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    // logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }
}
