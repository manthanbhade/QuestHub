package com.example.QuoraBackend.controller;

import com.example.QuoraBackend.dto.request.CreateQuestionRequest;
import com.example.QuoraBackend.dto.response.QuestionResponse;
import com.example.QuoraBackend.model.Question;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    //  create
    @PostMapping
    public ResponseEntity<?> createQuestion(
            @Valid @RequestBody CreateQuestionRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();

        Question savedQuestion =
                questionService.createQuestion(request, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "id", savedQuestion.getId(),
                        "message", "Question posted successfully ! You will also Get Question By its id "
                )
        );
    }


    //  READ (NON-PAGINATED)
    // Returns Response DTOs only
    @GetMapping("/all")
    public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
        return ResponseEntity.ok(
                questionService.getAllQuestions()
        );
    }

    //  READ (SINGLE)
    // Returns Response DTO only
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestionById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                questionService.getQuestionById(id)
        );
    }

    //UPDATE
    // Accepts request DTO, does not return entity
    @PutMapping("/{id}")
    public ResponseEntity<String> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody CreateQuestionRequest request
    ) {
        questionService.updateQuestion(id, request);
        return ResponseEntity.ok("You have successfully updated your question");
    }

    //READ (PAGINATED)
    // Page<QuestionResponse>, never Page<Question>

    @GetMapping
    public ResponseEntity<Page<QuestionResponse>> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                questionService.getAllQuestions(page, size)
        );
    }

    // delete question
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("You have successfully deleted your question");
    }

}
