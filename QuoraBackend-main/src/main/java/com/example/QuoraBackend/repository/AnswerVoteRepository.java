package com.example.QuoraBackend.repository;

import com.example.QuoraBackend.model.Answer;
import com.example.QuoraBackend.model.AnswerVote;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.model.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerVoteRepository extends JpaRepository<AnswerVote, Long> {

    // Find if user already voted on this answer
    Optional<AnswerVote> findByUserAndAnswer(User user, Answer answer);

    // Count votes by type (UPVOTE / DOWNVOTE)
    long countByAnswerAndVoteType(Answer answer, VoteType voteType);

    // Count all votes for answer (optional utility)
    long countByAnswer(Answer answer);
}
