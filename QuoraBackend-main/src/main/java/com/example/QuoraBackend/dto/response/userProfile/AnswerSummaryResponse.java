package com.example.QuoraBackend.dto.response.userProfile;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerSummaryResponse {

    private Long id;
    private String content;
    private Long questionId;
    private LocalDateTime createdAt;
}
