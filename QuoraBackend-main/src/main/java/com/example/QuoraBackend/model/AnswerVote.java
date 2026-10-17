package com.example.QuoraBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "answer_votes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "answer_id"})
        },
        indexes = {
                @Index(name = "idx_answer_vote", columnList = "answer_id"),
                @Index(name = "idx_user_answer_vote", columnList = "user_id")
        }
)
@Getter
@Setter
public class AnswerVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_answer_vote_user"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "answer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_answer_vote_answer"))
    private Answer answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
