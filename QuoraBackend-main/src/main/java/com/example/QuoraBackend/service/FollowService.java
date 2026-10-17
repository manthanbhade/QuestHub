package com.example.QuoraBackend.service;

import com.example.QuoraBackend.dto.response.FollowResponse;
import com.example.QuoraBackend.dto.response.UserResponse;
import com.example.QuoraBackend.model.Follow;
import com.example.QuoraBackend.model.NotificationType;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.repository.FollowRepository;
import com.example.QuoraBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserService userService; //  use UserService, not AuthService
    private final NotificationService notificationService;


    public FollowResponse followUser(Long targetUserId) {

        //  Get currently logged-in user properly
        User currentUser = userService.getCurrentAuthenticatedUser();

        //  Fetch target user
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  Cannot follow yourself
        if (currentUser.getUserId().equals(targetUserId)) {
            throw new RuntimeException("You cannot follow yourself");
        }

        //  Already following
        if (followRepository.existsByFollowerAndFollowing(currentUser, targetUser)) {
            throw new RuntimeException("Already following this user");
        }

        //  Create follow relation
        Follow follow = new Follow();
        follow.setFollower(currentUser);
        follow.setFollowing(targetUser);
        follow.setFollowedAt(LocalDateTime.now());

        followRepository.save(follow);

        //notification trigger
        if (!currentUser.getUserId().equals(targetUser.getUserId())) {
            notificationService.createNotification(
                    targetUser,
                    currentUser.getUsername() + " started following you.",
                    NotificationType.FOLLOW
            );
        }

        return new FollowResponse("Followed successfully");
    }


    public FollowResponse unfollowUser(Long targetUserId) {

        User currentUser = userService.getCurrentAuthenticatedUser();

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        followRepository.deleteByFollowerAndFollowing(currentUser, targetUser);

        return new FollowResponse("Unfollowed successfully");
    }

    // method to get all the followers
    public List<UserResponse> getFollowers(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return followRepository.findByFollowing(user)
                .stream()
                .map(Follow::getFollower) // get follower user
                .map(userService::mapToUserResponse)
                .collect(Collectors.toList());
    }

    // method to get following list
    public List<UserResponse> getFollowing(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return followRepository.findByFollower(user)
                .stream()
                .map(Follow::getFollowing)  // get the user being followed
                .map(userService::mapToUserResponse)
                .toList();
    }

}

