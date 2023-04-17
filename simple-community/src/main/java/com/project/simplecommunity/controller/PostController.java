package com.project.simplecommunity.controller;

import com.project.simplecommunity.domain.searchtype.SearchType;
import com.project.simplecommunity.dto.UserPrincipal;
import com.project.simplecommunity.dto.reqeust.PostRequest;
import com.project.simplecommunity.dto.response.PostResponse;
import com.project.simplecommunity.pagination.PageResponseDto;
import com.project.simplecommunity.pagination.SingleResponseDto;
import com.project.simplecommunity.repository.UserAccountRepository;
import com.project.simplecommunity.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//TODO: 댓글 관련 코드 추가해야 함
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserAccountRepository userAccountRepository; // 임시

    @GetMapping
    public ResponseEntity<PageResponseDto<PostResponse>> searchPosts(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchKeyword,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostResponse> posts = postService.searchPosts(searchType, searchKeyword, pageable);
        return new ResponseEntity<>(new PageResponseDto<>(posts.toList(), posts), HttpStatus.OK);
    }

    @GetMapping("/search-hashtags")
    public ResponseEntity<PageResponseDto<PostResponse>> searchPostsViaHashtag(
            @RequestBody(required = false) List<String> hashtags,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostResponse> posts = postService.searchPostsViaHashtag(hashtags, pageable);
        return new ResponseEntity<>(new PageResponseDto<>(posts.toList(), posts), HttpStatus.OK);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<SingleResponseDto<PostResponse>> getPost(@PathVariable("post-id") Long postId) {
        PostResponse post = postService.searchPost(postId);
        return new ResponseEntity<>(new SingleResponseDto<>(post), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SingleResponseDto<PostResponse>> createPost(
            @RequestPart(value = "request") PostRequest request,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles
            //@AuthenticationPrincipal UserPrincipal principal
    ) {
        PostResponse post = postService.createPost(request, imageFiles, getUserPrincipal());
        return new ResponseEntity<>(new SingleResponseDto<>(post), HttpStatus.CREATED);
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity<SingleResponseDto<PostResponse>> updatePost(
            @PathVariable("post-id") Long postId,
            @RequestPart(value = "request") PostRequest request,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> multipartFiles
            //@AuthenticationPrincipal UserPrincipal principal
    ) {
        PostResponse post = postService.updatePost(postId, request, multipartFiles, getUserPrincipal());
        return new ResponseEntity<>(new SingleResponseDto<>(post), HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<Void> deletePost(@PathVariable("post-id") Long postId
                                           //@AuthenticationPrincipal UserPrincipal principal
    ) {

        postService.deletePost(postId, getUserPrincipal().username());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/like/{post-id}")
    public ResponseEntity<SingleResponseDto<PostResponse>> registerPostLike(
            @PathVariable("post-id") Long postId,
            @AuthenticationPrincipal UserPrincipal principal)
    {
        PostResponse post = postService.registerPostLike(postId, principal);
        return new ResponseEntity<>(new SingleResponseDto<>(post), HttpStatus.OK);
    }

    @DeleteMapping("/like/{post-id}")
    public ResponseEntity<SingleResponseDto<PostResponse>> deletePostLike(
            @PathVariable("post-id") Long postId,
            @AuthenticationPrincipal UserPrincipal principal)
    {
        PostResponse post = postService.deletePostLike(postId, principal);
        return new ResponseEntity<>(new SingleResponseDto<>(post), HttpStatus.OK);
    }

    // 임시
    private UserPrincipal getUserPrincipal() {
        return userAccountRepository.findById("juno")
                .map(UserPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않음."));
    }

}
