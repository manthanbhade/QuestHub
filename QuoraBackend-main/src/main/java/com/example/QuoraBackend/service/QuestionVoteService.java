package com.example.QuoraBackend.service;

import com.example.QuoraBackend.dto.request.VoteRequest;
import com.example.QuoraBackend.exception.ResourceNotFoundException;
import com.example.QuoraBackend.model.*;
import com.example.QuoraBackend.repository.QuestionRepository;
import com.example.QuoraBackend.repository.QuestionVoteRepository;
import com.example.QuoraBackend.repository.UserRepository;
import com.example.QuoraBackend.security.util.ReputationRules;
import com.example.QuoraBackend.security.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class QuestionVoteService {

    private final QuestionVoteRepository questionVoteRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    public QuestionVoteService(
            QuestionVoteRepository questionVoteRepository,
            QuestionRepository questionRepository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.questionVoteRepository = questionVoteRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }


    // vote
    // Create or update vote (idempotent)
    @Transactional
    public void vote(Long questionId, VoteRequest request) {

        User currentUser = getCurrentAuthenticatedUser();

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        questionVoteRepository
                .findByUserAndQuestion(currentUser, question)
                .ifPresentOrElse(
                        existingVote -> updateVote(existingVote, request.getVoteType()),
                        () -> createVote(currentUser, question, request.getVoteType())
                );
    }

    // unvote
    // Remove current user's vote (if exists)

    @Transactional
    public void unvote(Long questionId) {

        User currentUser = getCurrentAuthenticatedUser();

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        questionVoteRepository.deleteByUserAndQuestion(currentUser, question);
    }

    // internal
    private void updateVote(QuestionVote existingVote, VoteType newVoteType) {
        if (existingVote.getVoteType() != newVoteType) {
            existingVote.setVoteType(newVoteType);
            questionVoteRepository.save(existingVote);
        }
    }

    private void createVote(User user, Question question, VoteType voteType) {
        QuestionVote vote = new QuestionVote();
        vote.setUser(user);
        vote.setQuestion(question);
        vote.setVoteType(voteType);
        questionVoteRepository.save(vote);
    }

    @Transactional
    public void vote(Long questionId, VoteType voteType) {

        User voter = getCurrentAuthenticatedUser();

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        User owner = question.getUser(); // reputation goes to this user

        Optional<QuestionVote> existingVote =
                questionVoteRepository.findByUserAndQuestion(voter, question);

        //  FIRST TIME VOTE
        if (existingVote.isEmpty()) {

            QuestionVote vote = new QuestionVote();
            vote.setUser(voter);
            vote.setQuestion(question);
            vote.setVoteType(voteType);
            vote.setCreatedAt(LocalDateTime.now());

            questionVoteRepository.save(vote);
            applyReputation(owner, voteType);

            // Notification ONLY for first-time UPVOTE and not self
            if (voteType == VoteType.UPVOTE &&
                    !voter.getUserId().equals(owner.getUserId())) {

                notificationService.createNotification(
                        owner,
                        voter.getUsername() + " upvoted your question.",
                        NotificationType.UPVOTE_QUESTION
                );
            }
            return;
        }

        QuestionVote vote = existingVote.get();

        //  CHANGE VOTE (UP ↔ DOWN)
        if (vote.getVoteType() != voteType) {

            revertReputation(owner, vote.getVoteType());
            applyReputation(owner, voteType);

            vote.setVoteType(voteType);
            questionVoteRepository.save(vote);
        }
        //  SAME VOTE AGAIN → UNVOTE
        else {
            revertReputation(owner, vote.getVoteType());
            questionVoteRepository.delete(vote);
        }
    }

    // reputation helper methods
    private void applyReputation(User user, VoteType type) {

        if (type == VoteType.UPVOTE) {
            user.setReputation(
                    user.getReputation() + ReputationRules.UPVOTE_RECEIVED
            );
        } else {
            user.setReputation(
                    user.getReputation() + ReputationRules.DOWNVOTE_RECEIVED
            );
        }

        userRepository.save(user);
    }

    private void revertReputation(User user, VoteType type) {

        if (type == VoteType.UPVOTE) {
            user.setReputation(
                    user.getReputation() - ReputationRules.UPVOTE_RECEIVED
            );
        } else {
            user.setReputation(
                    user.getReputation() - ReputationRules.DOWNVOTE_RECEIVED
            );
        }

        userRepository.save(user);
    }

    //Get current authenticated user
    private User getCurrentAuthenticatedUser() {

        String email = SecurityUtils.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
