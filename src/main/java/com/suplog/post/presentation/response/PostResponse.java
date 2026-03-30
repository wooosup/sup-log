package com.suplog.post.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.suplog.post.application.dto.PostResult;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime createDateTime
) {

    public static PostResponse from(PostResult result) {
        return new PostResponse(
                result.id(),
                result.title(),
                result.content(),
                result.createDateTime()
        );
    }
}
