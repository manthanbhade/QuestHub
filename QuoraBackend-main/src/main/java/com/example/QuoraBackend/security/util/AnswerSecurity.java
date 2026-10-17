package com.example.QuoraBackend.security.util;

import com.example.QuoraBackend.repository.AnswerRepository;
import org.springframework.stereotype.Component;

@Component
public class AnswerSecurity {

    private final AnswerRepository answerRepository;

    public AnswerSecurity(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public boolean isOwner(Long answerId) {
        String currentUserEmail = SecurityUtils.getCurrentUserEmail();

        return answerRepository.findById(answerId)
                .map(a -> a.getUser().getEmail().equals(currentUserEmail))
                .orElse(false);
    }
}
