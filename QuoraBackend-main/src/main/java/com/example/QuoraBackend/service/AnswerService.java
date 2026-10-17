package com.example.QuoraBackend.service;

import com.example.QuoraBackend.dto.response.AnswerResponse;
import com.example.QuoraBackend.dto.response.UserResponse;
import com.example.QuoraBackend.dto.response.VoteResponse;
import com.example.QuoraBackend.exception.ResourceNotFoundException;
import com.example.QuoraBackend.model.*;
import com.example.QuoraBackend.repository.*;
import com.example.QuoraBackend.security.util.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AnswerVoteRepository answerVoteRepository;
    private final NotificationService notificationService;



    public AnswerService(AnswerRepository answerRepository,
                         QuestionRepository questionRepository,
                         UserRepository userRepository,
                         UserService userService,
                         AnswerVoteRepository answerVoteRepository,
                         NotificationService notificationService) {

        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.answerVoteRepository=answerVoteRepository;
        this.notificationService=notificationService;
    }

    public AnswerResponse createAnswer(Long questionId, String content) {

        User currentUser = getCurrentAuthenticatedUser();

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Answer answer = new Answer();
        answer.setContent(content);
        answer.setUser(currentUser);
        answer.setQuestion(question);
        answer.setCreatedAt(LocalDateTime.now());

        Answer savedAnswer = answerRepository.save(answer);

        //  Reputation rule: +2 for giving an answer
        // Notify question owner (avoid self-notification)
        if (!currentUser.getUserId().equals(question.getUser().getUserId())) {
            notificationService.createNotification(
                    question.getUser(),
                    currentUser.getUsername() + " answered your question.",
                    NotificationType.ANSWER
            );
        }

        currentUser.setReputation(currentUser.getReputation() + 2);
        userRepository.save(currentUser);

        return mapToAnswerResponse(savedAnswer);
    }



    //read
    // Read path returns Response DTOs only

    public List<AnswerResponse> getAnswersByQuestionId(Long questionId) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        return answerRepository.findByQuestion(question)
                .stream()
                .map(this::mapToAnswerResponse)
                .toList();
    }

    // delete
    // Authorization enforced at service layer

    @PreAuthorize("hasRole('ADMIN') or @answerSecurity.isOwner(#answerId)")
    public void deleteAnswer(Long answerId) {

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

        answerRepository.delete(answer);
    }

    // mappers
    // Mapping is explicit, private, and service-owned

    private AnswerResponse mapToAnswerResponse(Answer answer) {
        return new AnswerResponse(
                answer.getId(),
                answer.getContent(),
                userService.mapToUserResponse(answer.getUser()),
                answer.getCreatedAt()
        );
    }

    public UserResponse getAnswerAuthor(Long answerId) {

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        User user = answer.getUser();

        //  RETURN MUST BE INSIDE METHOD
        return userService.mapToUserResponse(user);
    }

// for deleting this method
    private User getCurrentAuthenticatedUser() {

        String email = SecurityUtils.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }


    public List<AnswerResponse> getMyAnswers() {

        User currentUser = getCurrentAuthenticatedUser();

        return answerRepository.findByUser(currentUser)
                .stream()
                .map(this::mapToAnswerResponse)
                .toList();
    }

    // answer by other user
    public List<AnswerResponse> getAnswersByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return answerRepository.findByUser(user)
                .stream()
                .map(this::mapToAnswerResponse)
                .toList();
    }

    //updating answer
    @PreAuthorize("hasRole('ADMIN') or @answerSecurity.isOwner(#answerId)")
    public void updateAnswer(Long answerId, String content) {

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Answer not found"));

        answer.setContent(content);

        answerRepository.save(answer);
    }

    @Transactional
    public VoteResponse voteAnswer(Long answerId, VoteType voteType) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        Optional<AnswerVote> existingVote =
                answerVoteRepository.findByUserAndAnswer(currentUser, answer);

        String action;

        if (existingVote.isPresent()) {

            AnswerVote vote = existingVote.get();

            if (vote.getVoteType() == voteType) {

                answerVoteRepository.delete(vote);
                updateReputation(answer.getUser(), voteType, true);
                action = "REMOVED";

            } else {

                VoteType oldType = vote.getVoteType();
                vote.setVoteType(voteType);
                answerVoteRepository.save(vote);

                updateReputation(answer.getUser(), oldType, true);
                updateReputation(answer.getUser(), voteType, false);

                action = "CHANGED";
            }

        } else {

            AnswerVote vote = new AnswerVote();
            vote.setUser(currentUser);
            vote.setAnswer(answer);
            vote.setVoteType(voteType);

            answerVoteRepository.save(vote);
            updateReputation(answer.getUser(), voteType, false);
            // Notify ONLY for first-time UPVOTE and not self
            if (voteType == VoteType.UPVOTE &&
                    !currentUser.getUserId().equals(answer.getUser().getUserId())) {

                notificationService.createNotification(
                        answer.getUser(),
                        currentUser.getUsername() + " upvoted your answer.",
                        NotificationType.UPVOTE_ANSWER
                );
            }

            action = voteType == VoteType.UPVOTE ? "UPVOTED" : "DOWNVOTED";
        }

        long upvotes = answerVoteRepository
                .countByAnswerAndVoteType(answer, VoteType.UPVOTE);

        long downvotes = answerVoteRepository
                .countByAnswerAndVoteType(answer, VoteType.DOWNVOTE);

        return new VoteResponse(
                action,
                upvotes,
                downvotes,
                answer.getUser().getReputation()
        );
    }


    private void updateReputation(User owner, VoteType type, boolean isRemoving) {

        int value = (type == VoteType.UPVOTE) ? 10 : -2;

        if (isRemoving) {
            owner.setReputation(owner.getReputation() - value);
        } else {
            owner.setReputation(owner.getReputation() + value);
        }

        userRepository.save(owner);
    }
}
