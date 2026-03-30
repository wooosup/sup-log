package com.suplog.post.presentation.request;

import com.suplog.post.application.dto.PostCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 20, message = "제목은 20글자 이하로 입력해주세요.")
        String title,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {

    public PostCreateCommand toCommand() {
        return new PostCreateCommand(title, content);
    }
}
