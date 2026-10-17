package com.example.QuoraBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateQuestionRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 150, message = "Title must be 5–150 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 5000, message = "Content must be 10–5000 characters")
    private String content;

    private List<String> tags;
}
