package com.suplog.docs;

import com.suplog.post.application.PostService;
import com.suplog.post.application.dto.PostCreateCommand;
import com.suplog.post.application.dto.PostResult;
import com.suplog.post.presentation.PostController;
import com.suplog.post.presentation.request.PostCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerDocsTest extends RestDocsSupport{

    public static final long POST_ID = 1L;
    public static final LocalDateTime DATE_TIME = LocalDateTime.of(2024, 12, 20, 12, 0);
    private final PostService postService = mock(PostService.class);

    @Override
    protected Object initController() {
        return new PostController(postService);
    }

    @DisplayName("신규 게시글을 작성하는 API")
    @Test
    void createPost() throws Exception {
        // 요청 객체 생성
        PostCreateRequest request = new PostCreateRequest("제목", "내용");

        // 응답 객체 정의
        given(postService.write(any(PostCreateCommand.class)))
                .willReturn(mockPostResponse());

        // MockMvc 테스트
        mockMvc.perform(
                        post("/posts")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("게시글 내용")
                        ),
                        commonResponseFields()
                ));
    }


    @DisplayName("게시글 조회 API")
    @Test
    void getPost() throws Exception {
        given(postService.findPost(POST_ID))
                .willReturn(mockPostResponse());

        mockMvc.perform(get("/posts/{postId}", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        commonResponseFields()
                ));
    }

    private PostResult mockPostResponse() {
        return new PostResult(POST_ID, "제목", "내용", DATE_TIME);
    }

    private static ResponseFieldsSnippet commonResponseFields() {
        return responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 내용"),
                fieldWithPath("data.createDateTime").type(JsonFieldType.STRING).description("게시글 생성 시간")
        );
    }
}
