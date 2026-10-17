//package com.example.QuoraBackend.model;
//import com.example.QuoraBackend.model.Role;
//
//import jakarta.persistence.*;
//import lombok.*;
////import org.aspectj.weaver.patterns.TypePatternQuestions;
//
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Getter                      // Generates getters for all fields (safe for JPA)
//@Setter                      // Generates setters (used carefully via service layer)
//@NoArgsConstructor           // Required by JPA for entity instantiation
//@Entity                       // Marks this class as a JPA entity (mapped to DB table)
//@AllArgsConstructor
//@Builder
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)   // Auto-increment primary key
//    private Long userId;
//
//    private String username;      // Human-readable user identifier (no auth yet)
//
//    private String email;         // Contact identity (unique later, not enforced now)
//
//    @Column(nullable = false)
//    private String password;  // stores hash password not plain
//
//    @Column(length = 200)
//    private String bio;  // adding bio
//
////    @Enumerated(EnumType.STRING)
////    @Column(nullable = false)
////    private com.example.quorabackend.security.RoleType role;
//
//    @OneToMany(
//            mappedBy = "user",            // Inverse side; FK lives in Question table
//            cascade = CascadeType.ALL,    // User lifecycle controls Questions
//            orphanRemoval = true          // Removing a Question deletes it from DB
//    )
//    private List<Question> questions = new ArrayList<>(); // All questions created by user
//
//    @OneToMany(
//            mappedBy = "user",            // Inverse side; FK lives in Answer table
//            cascade = CascadeType.ALL,    // User lifecycle controls Answers
//            orphanRemoval = true          // Removing an Answer deletes it from DB
//    )
//    private List<Answer> answers = new ArrayList<>();     // All answers written by user
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();
//
//
//}

package com.example.QuoraBackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor               // Required by JPA
@AllArgsConstructor              // Useful for testing / internal use
@Entity
@Table(name = "users")           // Optional but recommended (avoids reserved keywords)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;      // hashed password only

    @Column(nullable = false)
    private int reputation = 0;   // Credit of the user


    @Column(length = 200)
    private String bio;            // user bio (profile)


    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Question> questions = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Answer> answers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}

