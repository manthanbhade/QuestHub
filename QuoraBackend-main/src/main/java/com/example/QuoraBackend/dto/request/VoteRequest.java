package com.example.QuoraBackend.dto.request;

import com.example.QuoraBackend.model.VoteType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VoteRequest {

    @NotNull
    private VoteType voteType;
}
