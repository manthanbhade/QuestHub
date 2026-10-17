package com.example.QuoraBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Short summary of the question
    @Column(nullable = false, length = 150)
    private String title;

    // Full question body
    @Column(nullable = false, length = 5000)
    private String content;

    // Creation timestamp
    private LocalDateTime createdAt;

    // Auto-populate createdAt before insert
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Answers belonging to this question
    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Answer> answers = new ArrayList<>();

    // Owner of the question
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "question_tags",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();


}
