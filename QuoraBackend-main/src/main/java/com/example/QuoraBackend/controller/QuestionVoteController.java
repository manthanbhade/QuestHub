package com.example.QuoraBackend.controller;

import com.example.QuoraBackend.dto.request.VoteRequest;
import com.example.QuoraBackend.model.VoteType;
import com.example.QuoraBackend.service.QuestionVoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class QuestionVoteController {

    private final QuestionVoteService questionVoteService;

    public QuestionVoteController(QuestionVoteService questionVoteService) {
        this.questionVoteService = questionVoteService;
    }

    // vote
    // Upvote or downvote a question
    @PostMapping("/{questionId}/vote")
    public ResponseEntity<Void> vote(
            @PathVariable Long questionId,
            @Valid @RequestBody VoteRequest request
    ) {
        questionVoteService.vote(questionId, request);
        return ResponseEntity.ok().build();
    }

    //  unvote
    // Remove current user's vote on a question
    @DeleteMapping("/{questionId}/vote")
    public ResponseEntity<Void> unvote(@PathVariable Long questionId) {
        questionVoteService.unvote(questionId);
        return ResponseEntity.noContent().build();
    }


    //private final QuestionVoteService questionVoteService;

    @PostMapping("/{id}/upvote")
    public ResponseEntity<String> upvote(@PathVariable Long id) {
        questionVoteService.vote(id, VoteType.UPVOTE);
        return ResponseEntity.ok("Upvoted");
    }

    @PostMapping("/{id}/downvote")
    public ResponseEntity<String> downvote(@PathVariable Long id) {
        questionVoteService.vote(id, VoteType.DOWNVOTE);
        return ResponseEntity.ok("Downvoted");
    }
}
