package com.example.QuoraBackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Marks this class as a source of Spring-managed beans
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt is used to hash passwords securely (one-way, salted, industry standard)
        // Defined as a bean so it can be injected wherever password hashing is needed
        return new BCryptPasswordEncoder();
    }
}
