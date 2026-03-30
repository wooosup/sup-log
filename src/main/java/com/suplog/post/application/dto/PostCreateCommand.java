package com.suplog.post.application.dto;

public record PostCreateCommand(
        String title,
        String content
) {
}
