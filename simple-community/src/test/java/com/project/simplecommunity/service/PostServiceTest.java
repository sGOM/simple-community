package com.project.simplecommunity.service;

import com.project.simplecommunity.domain.Hashtag;
import com.project.simplecommunity.domain.Post;
import com.project.simplecommunity.domain.PostHashtag;
import com.project.simplecommunity.domain.UserAccount;
import com.project.simplecommunity.domain.searchtype.SearchType;
import com.project.simplecommunity.dto.reqeust.PostRequest;
import com.project.simplecommunity.dto.reqeust.UserAccountDto;
import com.project.simplecommunity.dto.response.PostResponse;
import com.project.simplecommunity.exception.custom.PostException;
import com.project.simplecommunity.repository.HashtagRepository;
import com.project.simplecommunity.repository.PostHashtagRepository;
import com.project.simplecommunity.repository.PostRepository;
import com.project.simplecommunity.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

//TODO: 댓글 관련 코드 추가해야 함
@DisplayName("포스트 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks PostService sut;
    @Mock PostRepository postRepository;
    @Mock UserAccountRepository userAccountRepository;
    @Mock HashtagRepository hashtagRepository;
    @Mock PostHashtagRepository postHashtagRepository;

    @DisplayName("검색어 없이 게시글 검색 시, 포스트 페이지 반환")
    @Test
    void givenNoSearchKeyword_whenSearchingPosts_thenReturnPostPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(postRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<PostResponse> posts = sut.searchPosts(null, null, pageable);

        // Then
        assertThat(posts).isEmpty();
        then(postRepository).should().findAll(pageable);
    }

    @DisplayName("검색어로 게시글 검색 시, 검색된 포스트 페이지 반환")
    @Test
    void givenSearchKeyword_whenSearchingPosts_thenReturnSearchedPostPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title1";
        given(postRepository.findBySearchTypeAndSearchKeyword(searchType, searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<PostResponse> posts = sut.searchPosts(searchType, searchKeyword, pageable);

        // Then
        assertThat(posts).isEmpty();
        then(postRepository).should().findBySearchTypeAndSearchKeyword(searchType, searchKeyword, pageable);
    }

    @DisplayName("해시태그 없이 게시글 검색 시, 빈 페이지 반환")
    @Test
    void givenNoHashtag_whenSearchingViaPosts_thenReturnEmptyPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);

        // When
        Page<PostResponse> posts = sut.searchPostsViaHashtag(null, pageable);

        // Then
        assertThat(posts).isEqualTo(Page.empty(pageable));
        then(postRepository).shouldHaveNoInteractions();
    }

    @DisplayName("없는 해시태그로 게시글 검색 시, 빈 페이지 반환")
    @Test
    void givenNonexistentHashtag_whenSearchingViaPosts_thenReturnEmptyPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        List<String> hashtagNames = List.of("존재하지 않는 해시태그");
        given(postRepository.findByHashtagNames(hashtagNames, pageable)).willReturn(new PageImpl<>(List.of(), pageable, 0));

        // When
        Page<PostResponse> posts = sut.searchPostsViaHashtag(hashtagNames, pageable);

        // Then
        assertThat(posts).isEqualTo(Page.empty(pageable));
        then(postRepository).should().findByHashtagNames(hashtagNames, pageable);
    }

    @DisplayName("해시태그로 게시글 검색 시, 검색된 포스트 페이지 반환")
    @Test
    void givenHashtag_whenSearchingViaPosts_thenReturnSearchedPostPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        List<String> hashtagNames = List.of("hashtag");
        Post expected = createPost(createUserAccount());
        given(postRepository.findByHashtagNames(hashtagNames, pageable)).willReturn(new PageImpl<>(List.of(expected), pageable, 1));

        // When
        Page<PostResponse> posts = sut.searchPostsViaHashtag(hashtagNames, pageable);

        // Then
        assertThat(posts).isEqualTo(new PageImpl<>(List.of(PostResponse.from(expected)), pageable, 1));
        then(postRepository).should().findByHashtagNames(hashtagNames, pageable);
    }

    @DisplayName("포스트 ID로 조회 시, 해당 포스트 반환")
    @Test
    void givenPostId_whenSearchingPost_thenReturnPost() {
        // Given
        long postId = 1L;
        Post post = createPost(createUserAccount());
        Hashtag hashtag = createHashtag(1L, "hashtag");
        createPostHashtag(1L, post, hashtag);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // When
        PostResponse response = sut.searchPost(postId);

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("title", post.getTitle())
                .hasFieldOrPropertyWithValue("content", post.getContent())
                .hasFieldOrPropertyWithValue("hashtags", List.of("hashtag"));
        then(postRepository).should().findById(postId);
    }

    @DisplayName("해당 포스트가 없을 시, 예외 반환")
    @Test
    void givenNonexistentPostId_whenSearchingPost_thenThrowsException() {
        // Given
        long postId = 0L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.searchPost(postId));

        // Then
        assertThat(t)
                .isInstanceOf(PostException.class);
        then(postRepository).should().findById(postId);
    }

    @DisplayName("포스트 정보 입력 시, 포스트 생성")
    @Test
    void givenPostInfo_whenSavingPost_thenSavesPost() {
        // Given
        PostRequest postRequest = createPostRequest();
        given(postRepository.save(any(Post.class))).willReturn(createPost(createUserAccount()));
        given(hashtagRepository.save(any(Hashtag.class))).willReturn(createHashtag(1L, "hashtag"));
        given(postHashtagRepository.save(any(PostHashtag.class)))
                .willReturn(createPostHashtag(1L, createPost(createUserAccount()), createHashtag(1L, "hashtag")));

        // When
        sut.createPost(postRequest, null, UserAccountDto.from(createUserAccount()));

        // Then
        then(postRepository).should().save(any(Post.class));
        then(hashtagRepository).should().save(any(Hashtag.class));
        then(postHashtagRepository).should().save(any(PostHashtag.class));
    }

    @DisplayName("포스트의 수정 정보 입력 시, 포스트 수정 : 해시태그 정보 X")
    @Test
    void givenModifiedPostInfoNonHashtags_whenUpdatingPost_thenUpdatesPost() {
        // Given
        UserAccount userAccount = createUserAccount();
        Post post = createPost(userAccount);
        Hashtag hashtag = createHashtag(1L, "hashtag");
        createPostHashtag(1L, post, hashtag);

        PostRequest update = PostRequest.of("new title", "new content", null);

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(userAccountRepository.getReferenceById(userAccount.getUserId())).willReturn(userAccount);

        // When
        sut.updatePost(post.getId(), update, null, UserAccountDto.from(userAccount));

        // Then
        assertThat(post)
                .hasFieldOrPropertyWithValue("title", update.title())
                .hasFieldOrPropertyWithValue("content", update.content())
                .extracting("postHashtags", COLLECTION)
                    .hasSize(1)
                        .extracting("hashtag")
                            .extracting("hashtagName")
                            .containsExactly("hashtag");
        then(postRepository).should().findById(post.getId());
        then(userAccountRepository).should().getReferenceById(createUserAccount().getUserId());
    }

    @DisplayName("포스트의 수정 정보 입력 시, 포스트 수정 : 해시태그 정보 O")
    @Test
    void givenModifiedPostInfo_whenUpdatingPost_thenUpdatesPost() {
        // Given
        UserAccount userAccount = createUserAccount();
        Post post = createPost(userAccount);
        PostRequest update = PostRequest.of("new title", "new content", List.of("new hashtag"));

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(userAccountRepository.getReferenceById(userAccount.getUserId())).willReturn(userAccount);

        willDoNothing().given(postHashtagRepository).deleteAllByPost_Id(post.getId());
        willDoNothing().given(postRepository).flush();

        given(hashtagRepository.findByHashtagName(anyString())).willReturn(Optional.empty());
        given(hashtagRepository.save(any(Hashtag.class))).willReturn(createHashtag(1L, "new hashtag"));
        given(postHashtagRepository.save(any(PostHashtag.class)))
                .willReturn(createPostHashtag(2L, post, createHashtag(1L, "new hashtag")));

        // When
        sut.updatePost(post.getId(), update, null, UserAccountDto.from(userAccount));

        // Then
        assertThat(post)
                .hasFieldOrPropertyWithValue("title", update.title())
                .hasFieldOrPropertyWithValue("content", update.content())
                .extracting("postHashtags", COLLECTION)
                    .hasSize(1)
                            .extracting("hashtag")
                                    .extracting("hashtagName")
                                    .containsExactly("new hashtag");
        then(postRepository).should().findById(post.getId());
        then(userAccountRepository).should().getReferenceById(createUserAccount().getUserId());
        then(postHashtagRepository).should().deleteAllByPost_Id(post.getId());
        then(postRepository).should().flush();
        then(hashtagRepository).should().findByHashtagName(anyString());
        then(hashtagRepository).should().save(any(Hashtag.class));
        then(postHashtagRepository).should().save(any(PostHashtag.class));
    }

    @DisplayName("포스트 ID 입력 시, 포스트 삭제")
    @Test
    void givenPostId_whenDeletingPost_thenDeletesPost() {
        // Given
        long postId = 1L;
        String userId = "juno";
        willDoNothing().given(postRepository).deleteByIdAndUserAccount_UserId(postId, userId);

        // When
        sut.deletePost(postId, userId);

        // Then
        then(postRepository).should().deleteByIdAndUserAccount_UserId(postId, userId);
    }

    private PostRequest createPostRequest() {
        return PostRequest.of("title", "content", List.of("hashtag"));
    }

    private Post createPost(UserAccount userAccount) {
        Post post = createPostRequest().toEntity(userAccount);
        ReflectionTestUtils.setField(post, "id", 1L);
        return post;
    }

    private UserAccount createUserAccount() {
        return UserAccount.of("juno", "password", "mail@mail.com");
    }

    private Hashtag createHashtag(Long id, String hashtagName) {
        Hashtag hashtag = Hashtag.of(hashtagName);
        ReflectionTestUtils.setField(hashtag, "id", id);
        return hashtag;
    }

    private Hashtag createHashtag2(String hashtagName) {
        return Hashtag.of(hashtagName);
    }

    private PostHashtag createPostHashtag(long id, Post post, Hashtag hashtag) {
        PostHashtag postHashtag = PostHashtag.of(post, hashtag);

        post.getPostHashtags().add(postHashtag);
        hashtag.getPostHashtags().add(postHashtag);

        ReflectionTestUtils.setField(postHashtag, "id", id);
        return postHashtag;
    }

}
