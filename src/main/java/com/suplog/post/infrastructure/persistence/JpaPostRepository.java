package com.suplog.post.infrastructure.persistence;

import com.suplog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostRepository extends JpaRepository<Post, Long> {
}
