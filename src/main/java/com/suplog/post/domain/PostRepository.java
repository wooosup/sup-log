package com.suplog.post.domain;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll(PostSearchCriteria criteria);

    void delete(Post post);
}
