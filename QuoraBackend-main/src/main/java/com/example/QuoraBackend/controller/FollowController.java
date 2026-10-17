package com.example.QuoraBackend.controller;

import com.example.QuoraBackend.dto.response.FollowResponse;
import com.example.QuoraBackend.dto.response.UserResponse;
import com.example.QuoraBackend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{id}")
    public ResponseEntity<FollowResponse> followUser(@PathVariable Long id) {
        return ResponseEntity.ok(followService.followUser(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FollowResponse> unfollowUser(@PathVariable Long id) {
        return ResponseEntity.ok(followService.unfollowUser(id));
    }

    // api to get all the controller
    @GetMapping("/follow/users/{id}/followers")
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long id) {
        return ResponseEntity.ok(followService.getFollowers(id));
    }

}
