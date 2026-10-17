package com.example.QuoraBackend.controller;

import com.example.QuoraBackend.dto.request.ChangePasswordRequest;
import com.example.QuoraBackend.dto.request.UpdateProfileRequest;
import com.example.QuoraBackend.dto.response.UserResponse;
import com.example.QuoraBackend.dto.response.userProfile.UserProfileResponse;
import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.repository.UserRepository;
import com.example.QuoraBackend.service.FollowService;
import com.example.QuoraBackend.service.UserProfileService;
import com.example.QuoraBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserRepository userRepository;
    private final FollowService followService;
    private final UserService userService;

    @GetMapping("/me/profile")
    public UserProfileResponse getMyProfile(Authentication authentication) {

        // authentication.getName() == email
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userProfileService.getProfile(user.getUserId());
    }

    // update bio
    @PutMapping("/me/profile")
    public void updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request
    ) {
        String email = authentication.getName();
        userProfileService.updateBio(email, request.getBio());
    }

    // setting the bio to null or for frontend deleting
    @DeleteMapping("/me/bio")
    public ResponseEntity<String> deleteBio(Authentication authentication) {

        String email = authentication.getName();

        userProfileService.deleteBio(email);

        return ResponseEntity.ok("Bio deleted successfully");
    }

    // followers end point
    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long id) {
        return ResponseEntity.ok(followService.getFollowers(id));
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable Long id) {
        return ResponseEntity.ok(followService.getFollowing(id));
    }

    // api to change password
    @PutMapping("/me/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request
    ) {

        String email = authentication.getName();

        userService.changePassword(
                email,
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Password changed successfully");
    }

}


