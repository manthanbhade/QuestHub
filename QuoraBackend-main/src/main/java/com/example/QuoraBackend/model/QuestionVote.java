package com.example.QuoraBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "question_votes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "question_id"})
        },
        indexes = {
                @Index(name = "idx_question_vote", columnList = "question_id"),
                @Index(name = "idx_user_vote", columnList = "user_id")
        }
)

@Getter
@Setter
public class QuestionVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
