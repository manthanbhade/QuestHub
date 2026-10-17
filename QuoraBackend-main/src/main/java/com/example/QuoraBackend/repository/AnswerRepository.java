package com.example.QuoraBackend.repository;

import com.example.QuoraBackend.model.Answer;
import com.example.QuoraBackend.model.Question;
import com.example.QuoraBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // Fetch all answers belonging to a given question
    List<Answer> findByQuestion(Question question);


    // // Fetch all answers belonging to a given user
    List<Answer> findByUser(User user);


    List<Answer> findByUserUserId(Long userId);
}
