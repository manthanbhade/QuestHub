package com.example.QuoraBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionResponse {

    private Long id;
    private String title;
    private String content;
    private UserResponse author;
    private List<AnswerResponse> answers;
    private long upvotes;
    private long downvotes;
    private LocalDateTime createdAt;
    private List<String> tags;

}
