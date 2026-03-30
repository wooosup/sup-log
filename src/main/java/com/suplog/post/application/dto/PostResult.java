package com.suplog.post.application.dto;

import com.suplog.post.domain.Post;

import java.time.LocalDateTime;

public record PostResult(
        Long id,
        String title,
        String content,
        LocalDateTime createDateTime
) {

    public static PostResult from(Post post) {
        return new PostResult(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedDateTime()
        );
    }
}
