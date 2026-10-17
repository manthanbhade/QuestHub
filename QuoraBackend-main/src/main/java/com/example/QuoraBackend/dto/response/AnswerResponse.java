package com.example.QuoraBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnswerResponse {
    private Long id;
    private String content;
    private UserResponse author;
    private LocalDateTime createdAt;
}
