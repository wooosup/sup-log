package com.suplog.admin.application.dto;

public record LoginResult(
        String username,
        boolean authenticated
) {
}
