package com.example.QuoraBackend.repository;

import com.example.QuoraBackend.model.Question;
import com.example.QuoraBackend.model.QuestionVote;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.model.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionVoteRepository extends JpaRepository<QuestionVote, Long> {

    // Find existing vote by user on a question (idempotent voting)
    Optional<QuestionVote> findByUserAndQuestion(User user, Question question);

    // Count upvotes for a question
    long countByQuestionAndVoteType(Question question, VoteType voteType);

    // Delete vote (used when user switches vote)
    void deleteByUserAndQuestion(User user, Question question);
}
