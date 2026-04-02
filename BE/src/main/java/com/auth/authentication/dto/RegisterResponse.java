package com.auth.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

    private String accountId;
    private String username;
    private String email;
    private String role;
    private String message;
    private boolean success;
}
