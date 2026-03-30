package com.suplog.post.application;

import com.suplog.post.application.dto.PostCreateCommand;
import com.suplog.post.application.dto.PostResult;
import com.suplog.post.application.dto.PostUpdateCommand;
import com.suplog.post.domain.Post;
import com.suplog.post.domain.PostSearchCriteria;
import com.suplog.post.domain.exception.PostNotFoundException;
import com.suplog.post.infrastructure.persistence.JpaPostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private JpaPostRepository postRepository;

    @Test
    @DisplayName("글 작성")
    void writePost() {
        //given
        PostCreateCommand command = new PostCreateCommand("제목", "내용");

        //when
        postService.write(command);

        //then
        assertThat(postRepository.count()).isEqualTo(1);
        Post savedPost = postRepository.findAll().get(0);
        assertThat(savedPost.getTitle()).isEqualTo("제목");
        assertThat(savedPost.getContent()).isEqualTo("내용");
    }

    @DisplayName("게시글 생성 시 Auditing 테스트")
    @Test
    void auditingTest() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        Post savedPost = postRepository.save(post);

        //when & //then
        assertThat(savedPost.getCreatedDateTime()).isNotNull();
    }

    @Test
    @DisplayName("글 1개 조회")
    void find() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        Post savedPost = postRepository.save(post);

        //when
        PostResult response = postService.findPost(savedPost.getId());

        //then
        assertThat(post).isNotNull();
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.content()).isEqualTo("내용");
    }

    @Test
    @DisplayName("존재하지 않는 글을 조회하면 예외가 발생한다.")
    void findPostException() {
        //given
        Long postId = 1L;

        //when & then
        assertThatThrownBy(() -> postService.findPost(postId))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("글을 id값으로 내림차순해서 페이지를 조회한다.")
    void postPageableDesc() {
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .toList();

        postRepository.saveAll(requestPosts);

        PostSearchCriteria criteria = new PostSearchCriteria(1, 10);

        //when
        List<PostResult> posts = postService.findAll(criteria);

        //then
        assertThat(posts).hasSize(10);
        assertThat(posts.get(0).title()).isEqualTo("제목 19");
    }

    @Test
    @DisplayName("글 수정")
    void updatePost() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostUpdateCommand update = new PostUpdateCommand(null, "내용2");
        //when
        postService.update(post.getId(), update);

        //then
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용2");
    }

    @Test
    @DisplayName("글 삭제")
    void deletePost() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        //when
        postService.deletePost(post.getId());

        //then
        assertThat(postRepository.count()).isZero();
    }
}
