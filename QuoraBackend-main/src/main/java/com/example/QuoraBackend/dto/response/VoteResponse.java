package com.example.QuoraBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteResponse {

    private String action; // UPVOTED, DOWNVOTED, REMOVED, CHANGED
    private long upvotes;
    private long downvotes;
    private int ownerReputation;
}
