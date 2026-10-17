package com.example.QuoraBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAnswerRequest {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotBlank(message = "Answer content is required")
    @Size(min = 5, max = 5000, message = "Answer must be between 5 and 5000 characters")
    private String content;
}
