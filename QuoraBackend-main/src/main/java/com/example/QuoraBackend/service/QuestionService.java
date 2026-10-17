package com.example.QuoraBackend.service;

import com.example.QuoraBackend.dto.request.CreateQuestionRequest;
import com.example.QuoraBackend.dto.response.AnswerResponse;
import com.example.QuoraBackend.dto.response.QuestionResponse;
import com.example.QuoraBackend.dto.response.UserResponse;
import com.example.QuoraBackend.exception.ResourceNotFoundException;
import com.example.QuoraBackend.model.*;
import com.example.QuoraBackend.repository.QuestionRepository;
import com.example.QuoraBackend.repository.QuestionVoteRepository;
import com.example.QuoraBackend.repository.TagRepository;
import com.example.QuoraBackend.repository.UserRepository;
import com.example.QuoraBackend.security.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionVoteRepository questionVoteRepository;
    private final TagRepository tagRepository;
    private final UserService userService;
    private final NotificationService notificationService;



    public QuestionService(
            QuestionRepository questionRepository,
            UserRepository userRepository,
            QuestionVoteRepository questionVoteRepository,
            TagRepository tagRepository,
            UserService userService,
            NotificationService notificationService
    ) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.questionVoteRepository = questionVoteRepository;
        this.tagRepository = tagRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }


    // read
    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(this::mapToQuestionResponse)
                .toList();
    }

    public Page<QuestionResponse> getAllQuestions(int page, int size) {
        return questionRepository.findAll(
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        ).map(this::mapToQuestionResponse);
    }

    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        return mapToQuestionResponse(question);
    }

   //create
    public Question createQuestion(CreateQuestionRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Question question = new Question();
        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setCreatedAt(LocalDateTime.now());
        question.setUser(user);

        //  TAG HANDLING
//        Set<Tag> tags = new HashSet<>();
//
//        if (request.getTags() != null) {
//            for (String rawTag : request.getTags()) {
//
//                String tagName = rawTag.startsWith("#")
//                        ? rawTag.toLowerCase()
//                        : "#" + rawTag.toLowerCase();
//
//                Tag tag = tagRepository.findByName(tagName)
//                        .orElseGet(() -> {
//                            Tag newTag = new Tag();
//                            newTag.setName(tagName);
//                            newTag.setSlug(generateSlug(tagName));
//                            return newTag;
//                        });
//
//
//                tags.add(tag);
//            }
//        }
//        question.setTags(tags);
//
//        Question savedQuestion = questionRepository.save(question);
//
//        // Reputation rule: +1 for asking a question
//        user.setReputation(user.getReputation() + 1);
//        userRepository.save(user);
//
//        //return savedQuestion;
//
//        return questionRepository.save(question);
//
        Set<Tag> tags = new HashSet<>();

        if (request.getTags() != null) {
            for (String rawTag : request.getTags()) {

                String tagName = rawTag.startsWith("#")
                        ? rawTag.toLowerCase()
                        : "#" + rawTag.toLowerCase();

                String slug = generateSlug(tagName);

                Tag tag = tagRepository.findBySlug(slug)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(tagName);
                            newTag.setSlug(slug);
                            return tagRepository.save(newTag); //  save here
                        });

                tags.add(tag);
            }
        }

        question.setTags(tags);

        Question savedQuestion = questionRepository.save(question);

// Reputation rule: +1 for asking a question
        user.setReputation(user.getReputation() + 1);
        userRepository.save(user);

        return savedQuestion; // return once
    }

    // slug helper method
    private String generateSlug(String tagName) {
        return tagName
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", "") // remove special chars
                .trim()
                .replaceAll("\\s+", "-");     // spaces → dash
    }


    // update

    public void updateQuestion(Long id, CreateQuestionRequest request) {

        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        String email = SecurityUtils.getCurrentUserEmail();
        if (!existing.getUser().getEmail().equals(email)) {
            throw new IllegalStateException("You are not allowed to update this question");
        }

        existing.setTitle(request.getTitle());
        existing.setContent(request.getContent());

        questionRepository.save(existing);
    }

    //
    @PreAuthorize("hasRole('ADMIN') or @questionSecurity.isOwner(#questionId)")
    public void deleteQuestion(Long questionId) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        questionRepository.delete(question);
    }

  //votes

    @Transactional
    public void unvote(Long questionId) {

        User currentUser = getCurrentAuthenticatedUser();

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        questionVoteRepository.deleteByUserAndQuestion(currentUser, question);
    }


    // internal
    private User getCurrentAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    private UserResponse mapToUserResponse(User user) {
        //return new UserResponse(user.getUserId(), user.getUsername());
        return userService.mapToUserResponse(user);

    }

    private QuestionResponse mapToQuestionResponse(Question question) {

        List<String> tagNames = question.getTags()
                .stream()
                .map(Tag::getName)
                .toList();

        long upvotes = questionVoteRepository
                .countByQuestionAndVoteType(question, VoteType.UPVOTE);

        long downvotes = questionVoteRepository
                .countByQuestionAndVoteType(question, VoteType.DOWNVOTE);

        return new QuestionResponse(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                userService.mapToUserResponse(question.getUser()),
                question.getAnswers()
                        .stream()
                        .map(this::mapToAnswerResponse)
                        .toList(),
                upvotes,
                downvotes,
                question.getCreatedAt(),
                tagNames
        );
    }


    private AnswerResponse mapToAnswerResponse(Answer answer) {
        return new AnswerResponse(
                answer.getId(),
                answer.getContent(),
                userService.mapToUserResponse(answer.getUser()), //  FIXED
                answer.getCreatedAt()
        );
    }

}
