package com.example.QuoraBackend.dto.response.userProfile;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class QuestionSummaryResponse {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
}
