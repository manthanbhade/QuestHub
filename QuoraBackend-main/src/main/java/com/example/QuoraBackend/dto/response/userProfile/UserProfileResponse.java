//package com.example.QuoraBackend.dto.response.userProfile;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserProfileResponse {
//
//    private Long userId;
//    private String username;
//    private String email;
//    private String bio;
//
//    private int reputation;
//    private int questionCount;
//    private int answerCount;
//
//    private List<QuestionSummaryResponse> questions;
//    private List<AnswerSummaryResponse> answers;
//
//    private int totalQuestions;
//    private int totalAnswers;
//}

package com.example.QuoraBackend.dto.response.userProfile;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    private Long userId;
    private String username;
    private String email;
    private String bio;

    private int reputation;
    private int questionCount;
    private int answerCount;

    private List<QuestionSummaryResponse> questions;
    private List<AnswerSummaryResponse> answers;
}

