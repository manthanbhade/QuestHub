package com.example.QuoraBackend.controller;

import com.example.QuoraBackend.dto.request.CreateAnswerRequest;
import com.example.QuoraBackend.dto.request.UpdateAnswerRequest;
import com.example.QuoraBackend.dto.response.AnswerResponse;
import com.example.QuoraBackend.dto.response.VoteResponse;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.model.VoteType;
import com.example.QuoraBackend.service.AnswerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    // create
    // Controller accepts request DTO, returns no entity


    @PostMapping
    public ResponseEntity<String> createAnswer(
            @Valid @RequestBody CreateAnswerRequest request
    ) {
        answerService.createAnswer(
                request.getQuestionId(),
                request.getContent()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Your answer has been posted successfully");
    }

    // Read
    // Controller returns Response DTOs only

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByQuestionId(
            @PathVariable Long questionId
    ) {
        return ResponseEntity.ok(
                answerService.getAnswersByQuestionId(questionId)
        );
    }

    // get the answer by user
    @GetMapping("/me")
    public ResponseEntity<List<AnswerResponse>> getMyAnswers() {
        return ResponseEntity.ok(answerService.getMyAnswers());
    }

    // get answer by other user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                answerService.getAnswersByUser(userId)
        );
    }

    // deleting the answer by id
    @DeleteMapping("/{answerId}")
    public ResponseEntity<String> deleteAnswer(
            @PathVariable Long answerId
    ) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.ok("Answer deleted successfully");
    }

    // updating answer by answerId
    @PutMapping("/{answerId}")
    public ResponseEntity<String> updateAnswer(
            @PathVariable Long answerId,
            @Valid @RequestBody UpdateAnswerRequest request
    ) {
        answerService.updateAnswer(answerId, request.getContent());
        return ResponseEntity.ok("You have successfully updated your answer");
    }

    // voting response
    @PostMapping("/{id}/vote")
    public ResponseEntity<VoteResponse> voteAnswer(
            @PathVariable Long id,
            @RequestParam VoteType voteType
    ) {
        return ResponseEntity.ok(
                answerService.voteAnswer(id, voteType)
        );
    }
}
