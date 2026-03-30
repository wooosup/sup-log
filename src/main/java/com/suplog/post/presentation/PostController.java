package com.suplog.post.presentation;

import com.suplog.global.api.ApiResponse;
import com.suplog.post.application.PostService;
import com.suplog.post.presentation.request.PostCreateRequest;
import com.suplog.post.presentation.request.PostSearchRequest;
import com.suplog.post.presentation.request.PostUpdateRequest;
import com.suplog.post.presentation.response.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ApiResponse<PostResponse> post(@RequestBody @Valid PostCreateRequest request) {
        return ApiResponse.ok(PostResponse.from(postService.write(request.toCommand())));
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<PostResponse> findPost(@PathVariable Long postId) {
        return ApiResponse.ok(PostResponse.from(postService.findPost(postId)));
    }

    @GetMapping("/posts")
    public ApiResponse<List<PostResponse>> findAll(@ModelAttribute PostSearchRequest request) {
        return ApiResponse.ok(
                postService.findAll(request.toCriteria())
                        .stream()
                        .map(PostResponse::from)
                        .toList()
        );
    }

    @PatchMapping("/posts/{postId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable Long postId, @RequestBody @Valid PostUpdateRequest request) {
        return ApiResponse.ok(PostResponse.from(postService.update(postId, request.toCommand())));
    }

    @DeleteMapping("/posts/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ApiResponse.ok();
    }
}
