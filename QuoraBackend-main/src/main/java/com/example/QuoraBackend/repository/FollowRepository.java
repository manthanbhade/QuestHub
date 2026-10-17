package com.example.QuoraBackend.repository;

import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    void deleteByFollowerAndFollowing(User follower, User following);

    long countByFollowing(User user);

    long countByFollower(User user);

    List<Follow> findByFollowing(User user);

    List<Follow> findByFollower(User user);

    //void deleteByFollowerAndFollowing(User follower, User following);


}
