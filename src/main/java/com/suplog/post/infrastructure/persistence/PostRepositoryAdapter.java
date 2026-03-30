package com.suplog.post.infrastructure.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suplog.post.domain.Post;
import com.suplog.post.domain.PostRepository;
import com.suplog.post.domain.PostSearchCriteria;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.suplog.post.domain.QPost.post;

@Repository
public class PostRepositoryAdapter implements PostRepository {

    private final JpaPostRepository jpaPostRepository;
    private final JPAQueryFactory queryFactory;

    public PostRepositoryAdapter(JpaPostRepository jpaPostRepository, EntityManager entityManager) {
        this.jpaPostRepository = jpaPostRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Post save(Post post) {
        return jpaPostRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return jpaPostRepository.findById(id);
    }

    @Override
    public List<Post> findAll(PostSearchCriteria criteria) {
        return queryFactory.selectFrom(post)
                .limit(criteria.size())
                .offset(criteria.calculateOffset())
                .orderBy(post.id.desc())
                .fetch();
    }

    @Override
    public void delete(Post post) {
        jpaPostRepository.delete(post);
    }
}
