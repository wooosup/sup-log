package com.suplog.post.presentation.request;

import com.suplog.post.domain.PostSearchCriteria;

public record PostSearchRequest(
        Integer page,
        Integer size
) {

    public PostSearchCriteria toCriteria() {
        return new PostSearchCriteria(page, size);
    }
}
