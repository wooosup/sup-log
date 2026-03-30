package com.suplog.post.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suplog.post.domain.Post;
import com.suplog.admin.domain.Admin;
import com.suplog.admin.infrastructure.persistence.JpaAdminRepository;
import com.suplog.post.infrastructure.persistence.JpaPostRepository;
import com.suplog.post.presentation.request.PostCreateRequest;
import com.suplog.post.presentation.request.PostUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaPostRepository postRepository;

    @Autowired
    private JpaAdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        adminRepository.deleteAll();
        adminRepository.save(Admin.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .build());
    }

    private MockHttpSession login() throws Exception {
        return (MockHttpSession) mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new com.suplog.admin.presentation.request.LoginRequest("admin", "1234"))))
                .andExpect(status().isOk())
                .andReturn()
                .getRequest()
                .getSession(false);
    }

    @Test
    @DisplayName("글 작성 요청시 제목이 없으면 실패한다.")
    void titleNullTest() throws Exception {
        PostCreateRequest request = new PostCreateRequest(null, "내용입니다.");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .session(login())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청시 DB에 값이 저장된다.")
    void savedDB() throws Exception {
        // when
        PostCreateRequest request = new PostCreateRequest("제목입니다.", "내용입니다.");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .session(login())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertThat(postRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("글의 제목은 20글자 이하로 작성해야한다.")
    void validateTitleLength() throws Exception {
        PostCreateRequest request = new PostCreateRequest("1231552346246212312321451352346", "내용입니다.");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .session(login())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인하지 않으면 글 작성에 실패한다.")
    void unauthorizedCreatePost() throws Exception {
        PostCreateRequest request = new PostCreateRequest("제목입니다.", "내용입니다.");

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("글 여러 개 조회")
    void findAll() throws Exception {
        Post post1 = Post.builder()
                .title("123")
                .content("내용입니다.")
                .build();

        Post post2 = Post.builder()
                .title("123")
                .content("내용입니다.")
                .build();

        postRepository.saveAll(List.of(post1, post2));

        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void pageable() throws Exception {
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .toList();

        postRepository.saveAll(requestPosts);

        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("글 수정")
    void updatePost() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostUpdateRequest postUpdate = new PostUpdateRequest("제목2", "내용2");

        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .session(login())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdate)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void deletePost() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .session(login())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 글 조회")
    void findPost_Exception() throws Exception {

        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 글 수정")
    void updatePost_Exception() throws Exception {
        PostUpdateRequest postUpdate = new PostUpdateRequest("제목2", "내용2");

        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .session(login())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdate)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
