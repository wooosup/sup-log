package com.suplog.post.application;

import com.suplog.post.application.dto.PostCreateCommand;
import com.suplog.post.application.dto.PostResult;
import com.suplog.post.application.dto.PostUpdateCommand;
import com.suplog.post.domain.Post;
import com.suplog.post.domain.PostRepository;
import com.suplog.post.domain.PostSearchCriteria;
import com.suplog.post.domain.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResult write(PostCreateCommand command) {
        Post savedPost = postRepository.save(Post.of(command.title(), command.content()));
        return PostResult.from(savedPost);
    }

    @Transactional(readOnly = true)
    public PostResult findPost(Long id) {
        return PostResult.from(findPostById(id));
    }

    @Transactional(readOnly = true)
    public List<PostResult> findAll(PostSearchCriteria criteria) {
        return postRepository.findAll(criteria)
                .stream()
                .map(PostResult::from)
                .toList();
    }

    public PostResult update(Long id, PostUpdateCommand command) {
        Post post = findPostById(id);
        post.update(command.title(), command.content());
        return PostResult.from(post);
    }

    public void deletePost(Long id) {
        postRepository.delete(findPostById(id));
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
    }
}

