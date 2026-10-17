package com.example.QuoraBackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;   // education, sports, corruption

    @Column(nullable = false, unique = true, length = 50)
    private String slug;   // education, sports -> A slug was the short label of an article used internally.

    @ManyToMany(mappedBy = "tags")
    private Set<Question> questions = new HashSet<>();
}
