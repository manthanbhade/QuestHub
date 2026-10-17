package com.example.QuoraBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // Field that will be stored in DB
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // mapping with question
    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    // mapping with user (owner of the answer)
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
