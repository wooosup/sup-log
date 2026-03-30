package com.suplog.admin.presentation.response;

import com.suplog.admin.application.dto.LoginResult;

public record LoginResponse(
        String username,
        boolean authenticated
) {

    public static LoginResponse from(LoginResult result) {
        return new LoginResponse(result.username(), result.authenticated());
    }
}
