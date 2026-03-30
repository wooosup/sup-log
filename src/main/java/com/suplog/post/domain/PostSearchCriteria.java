package com.suplog.post.domain;

public record PostSearchCriteria(
        Integer page,
        Integer size
) {

    private static final int MAX_SIZE = 2000;

    public PostSearchCriteria {
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 10 : Math.min(size, MAX_SIZE);
    }

    public long calculateOffset() {
        return (long) (page - 1) * size;
    }
}
