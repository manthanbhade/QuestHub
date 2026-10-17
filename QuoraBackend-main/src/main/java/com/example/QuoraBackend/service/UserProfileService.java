package com.example.QuoraBackend.service;

import com.example.QuoraBackend.model.User;
import com.example.QuoraBackend.dto.response.userProfile.AnswerSummaryResponse;
import com.example.QuoraBackend.dto.response.userProfile.QuestionSummaryResponse;
import com.example.QuoraBackend.dto.response.userProfile.UserProfileResponse;
import com.example.QuoraBackend.repository.AnswerRepository;
import com.example.QuoraBackend.repository.QuestionRepository;
import com.example.QuoraBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public UserProfileResponse getProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<QuestionSummaryResponse> questions =
                questionRepository.findByUser(user)
                        .stream()
                        .map(q -> new QuestionSummaryResponse(
                                q.getId(),
                                q.getTitle(),
                                q.getCreatedAt()
                        ))
                        .toList();


        List<AnswerSummaryResponse> answers =
                answerRepository.findByUser(user)
                        .stream()
                        .map(a -> new AnswerSummaryResponse(
                                a.getId(),
                                a.getContent(),
                                a.getQuestion().getId(),
                                a.getCreatedAt()
                        ))
                        .toList();

        return new UserProfileResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getBio(),

                user.getReputation(),           //  reputation
                questions.size(),               // total question count
                answers.size(),                 // total answer count

                questions,
                answers
        );
    }

    // for updating the bio
    public void updateBio(String email, String bio) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBio(bio);
        userRepository.save(user);
    }

    // setting the bio to null or for frontend deleting
    public void deleteBio(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBio(null);   //  delete bio
        userRepository.save(user);
    }
}
