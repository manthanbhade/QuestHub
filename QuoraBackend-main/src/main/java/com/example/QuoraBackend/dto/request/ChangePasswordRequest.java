package com.example.QuoraBackend.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
}
