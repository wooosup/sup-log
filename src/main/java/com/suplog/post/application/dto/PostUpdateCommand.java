package com.suplog.post.application.dto;

public record PostUpdateCommand(
        String title,
        String content
) {
}
