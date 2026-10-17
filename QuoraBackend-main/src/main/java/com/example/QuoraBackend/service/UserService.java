package com.example.QuoraBackend.service;

import com.example.QuoraBackend.dto.response.UserResponse;
import com.example.QuoraBackend.exception.ResourceNotFoundException;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.repository.FollowRepository;
import com.example.QuoraBackend.repository.UserRepository;

import com.example.QuoraBackend.security.util.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       FollowRepository followRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));
    }

    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    //  NEW METHOD
    public UserResponse getUserResponseById(Long userId) {

        User user = getUserById(userId);

        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getUserId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());

        userResponse.setFollowersCount(
                followRepository.countByFollowing(user)
        );

        userResponse.setFollowingCount(
                followRepository.countByFollower(user)
        );

        return userResponse;
    }

    public UserResponse mapToUserResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        response.setFollowersCount(
                followRepository.countByFollowing(user)
        );

        response.setFollowingCount(
                followRepository.countByFollower(user)
        );

        return response;
    }

    public User getCurrentAuthenticatedUser() {

        String email = SecurityUtils.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found"));
    }

    // changing the password
    public void changePassword(String email, String currentPassword, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check old password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Encode new password
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }
}

