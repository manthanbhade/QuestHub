package com.example.QuoraBackend.security.util;

import com.example.QuoraBackend.repository.QuestionRepository;
import org.springframework.stereotype.Component;

@Component
public class QuestionSecurity {

    private final QuestionRepository questionRepository;

    public QuestionSecurity(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public boolean isOwner(Long questionId) {
        String currentUserEmail = SecurityUtils.getCurrentUserEmail();

        return questionRepository.findById(questionId)
                .map(q -> q.getUser().getEmail().equals(currentUserEmail))
                .orElse(false);
    }
}

