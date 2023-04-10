package com.project.simplecommunity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.simplecommunity.config.TestSecurityConfig;
import com.project.simplecommunity.domain.searchtype.SearchType;
import com.project.simplecommunity.dto.reqeust.PostRequest;
import com.project.simplecommunity.dto.reqeust.UserAccountDto;
import com.project.simplecommunity.dto.response.PostResponse;
import com.project.simplecommunity.service.store.FileSystemStorageService;
import com.project.simplecommunity.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: 댓글 관련 코드 추가해야 함
@DisplayName("포스트 컨트롤러 테스트")
@Import(TestSecurityConfig.class)
@WebMvcTest(PostController.class)
class PostControllerTest {
    private final MockMvc mvc;
    @MockBean PostService postService;
    @MockBean FileSystemStorageService storageService;

    public PostControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[GET] 포스트 페이지 - 정상 호출 : 검색어 X")
    @Test
    void givenNothing_whenRequestingPosts_thenReturnsPostPage() throws Exception {
        // Given
        given(postService.searchPosts(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(postService).should().searchPosts(eq(null), eq(null), any(Pageable.class));
    }

    @DisplayName("[GET] 포스트 페이지 - 정상 호출 : 검색어 O")
    @Test
    void givenSearchKeyword_whenSearchingPosts_thenReturnsPostPage() throws Exception {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        given(postService.searchPosts(eq(searchType), eq(searchKeyword), any(Pageable.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/posts")
                    .queryParam("searchType", searchType.name())
                    .queryParam("searchKeyword", searchKeyword)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(postService).should().searchPosts(eq(searchType), eq(searchKeyword), any(Pageable.class));
    }

    @DisplayName("[GET] 포스트 페이지 - 정상 호출 : 해시태그 X")
    @Test
    void givenNothing_whenRequestingPosts_thenReturnsEmptyPage() throws Exception {
        // Given
        given(postService.searchPostsViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/posts/search-hashtags"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(postService).should().searchPostsViaHashtag(eq(null), any(Pageable.class));
    }

    @DisplayName("[GET] 포스트 페이지 - 정상 호출 : 해시태그 O")
    @Test
    void givenHashtags_whenRequestingPosts_thenReturnsPostPage() throws Exception {
        // Given
        List<String> hashtags = List.of("hashtag");
        String json = new ObjectMapper().writeValueAsString(hashtags);
        given(postService.searchPostsViaHashtag(eq(hashtags), any(Pageable.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/posts/search-hashtags")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(postService).should().searchPostsViaHashtag(eq(hashtags), any(Pageable.class));
    }

    @DisplayName("[GET] 포스트 상세 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingPost_thenReturnsPost() throws Exception {
        // Given
        long postId = 1L;
        given(postService.searchPost(postId)).willReturn(createPostResponse());

        // When & Then
        mvc.perform(get("/posts/" + postId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(postService).should().searchPost(postId);
    }


    @DisplayName("[POST] 포스트 작성 - 정상 호출 : 파일 첨부 X")
    @Test
    void givenNewPostInfo_whenRequesting_thenSavesNewPost() throws Exception {
        // Given
        String json = new ObjectMapper().writeValueAsString(
                PostRequest.of("title", "content", List.of("hashtag")));
        MockMultipartFile jsonFile = new MockMultipartFile(
                "request", "", "application/json", json.getBytes(StandardCharsets.UTF_8));
        given(postService.createPost(any(PostRequest.class), eq(null), any(UserAccountDto.class))).willReturn(createPostResponse());

        // When & Then
        mvc.perform(multipart("/posts")
                        .file(jsonFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(postService).should().createPost(any(PostRequest.class), eq(null), any(UserAccountDto.class));
        then(storageService).shouldHaveNoInteractions();
    }

    @DisplayName("[POST] 포스트 작성 - 정상 호출 : 파일 첨부 O")
    @Test
    void givenNewPostInfoAndImageFiles_whenRequesting_thenSavesNewPostWithImages() throws Exception {
        // Given
        String json = new ObjectMapper().writeValueAsString(
                PostRequest.of("title", "content", List.of("hashtag")));
        MockMultipartFile jsonFile = new MockMultipartFile(
                "request", "", "application/json", json.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile imageFiles = new MockMultipartFile(
                "imageFiles",
                "imageFile.png",
                "image/png",
                "<<png data>>".getBytes());
        given(postService.createPost(any(PostRequest.class), anyList(), any(UserAccountDto.class))).willReturn(createPostResponse());

        // When & Then
        mvc.perform(multipart("/posts")
                        .file(imageFiles)
                        .file(jsonFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(postService).should().createPost(any(PostRequest.class), anyList(), any(UserAccountDto.class));
    }

    @DisplayName("[PATCH] 포스트 수정 - 정상 호출")
    @Test
    void givenModifiedPostInfo_whenRequesting_thenUpdatesPost() throws Exception {
        // Given
        long postId = 1L;
        String json = new ObjectMapper().writeValueAsString(
                PostRequest.of("new title", "new content", List.of("new hashtag")));
        MockMultipartFile jsonFile = new MockMultipartFile(
                "request", "", "application/json", json.getBytes(StandardCharsets.UTF_8));
        given(postService.updatePost(eq(postId), any(PostRequest.class), eq(null), any(UserAccountDto.class))).willReturn(createPostResponse());

        // When & Then
        mvc.perform(multipart(HttpMethod.PATCH, "/posts/" + postId) // multipart()는 POST 로 강제 배정되기 때문에 PATCH 설정 필요
                        .file(jsonFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        then(postService).should().updatePost(eq(postId), any(PostRequest.class), eq(null), any(UserAccountDto.class));
        then(storageService).shouldHaveNoInteractions();
    }

    @DisplayName("[DELETE] 포스트 삭제 - 정상 호출")
    @Test
    void givenPostIdToDelete_whenRequesting_thenDeletesPost() throws Exception {
        // Given
        long postId = 1L;
        String userId = "junoTest";
        willDoNothing().given(postService).deletePost(postId, userId);

        // When & Then
        mvc.perform(delete("/posts/" + postId))
                .andExpect(status().isNoContent());
        then(postService).should().deletePost(postId, userId);
    }

    private PostResponse createPostResponse() {
        return PostResponse.of(1L,
                "title",
                "content",
                "juno",
                List.of("hashtag"),
                LocalDateTime.now());
    }

}
