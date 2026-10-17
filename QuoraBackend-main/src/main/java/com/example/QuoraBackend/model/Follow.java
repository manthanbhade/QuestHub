//package com.example.QuoraBackend.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import org.springframework.security.core.userdetails.User;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(
//        name = "follows",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
//)
//@Data
//public class Follow {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "follower_id", nullable = false)
//    private User follower;
//
//    @ManyToOne
//    @JoinColumn(name = "following_id", nullable = false)
//    private User following;
//
//    private LocalDateTime followedAt;
//}
package com.example.QuoraBackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
)
@Data
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;   //  your model.User

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following;  //  your model.User

    private LocalDateTime followedAt;
}
