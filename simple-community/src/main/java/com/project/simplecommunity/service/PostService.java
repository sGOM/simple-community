package com.project.simplecommunity.service;

import com.project.simplecommunity.domain.Hashtag;
import com.project.simplecommunity.domain.Post;
import com.project.simplecommunity.domain.PostHashtag;
import com.project.simplecommunity.domain.UserAccount;
import com.project.simplecommunity.domain.searchtype.SearchType;
import com.project.simplecommunity.dto.UserPrincipal;
import com.project.simplecommunity.dto.reqeust.PostRequest;
import com.project.simplecommunity.dto.response.PostResponse;
import com.project.simplecommunity.exception.custom.PostException;
import com.project.simplecommunity.exception.custom.PostExceptionType;
import com.project.simplecommunity.repository.*;
import com.project.simplecommunity.service.store.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

//TODO: 댓글 관련 코드 추가해야 함, 좋아요 관련 코드 작성
@RequiredArgsConstructor
@Transactional
@Service
public class PostService {
    private final FileSystemStorageService storageService;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final LikesRepository likesRepository;
    private final UserAccountRepository userAccountRepository;

    //TODO: 이 로직은 필요가 없음, 이유 : 엔티티에 연관관계를 걸어 놓는 순간 포스트만 가져와도 관련된 정보를 가져올 때 JPA 알아서 조인해서 가져옴
    @Transactional(readOnly = true)
    public Page<PostResponse> searchPosts(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank())
            return postRepository.findAll(pageable)
                    .map(PostResponse::from); // map(PostResponse::from)을 실행할 때 JPA 알아서 조인해서 가져옴!!

        return postRepository.findBySearchTypeAndSearchKeyword(searchType, searchKeyword, pageable)
                .map(PostResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> searchPostsViaHashtag(List<String> hashtagNames, Pageable pageable) {
        if (hashtagNames == null || hashtagNames.isEmpty())
            return Page.empty(pageable);

        return postRepository.findByHashtagNames(hashtagNames, pageable)
                .map(PostResponse::from);
    }

    @Transactional(readOnly = true)
    public PostResponse searchPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        return PostResponse.from(post);
    }

    // TODO: 추후에 유저 정보 넣을 것
    public PostResponse createPost(PostRequest postRequest, List<MultipartFile> multipartFiles, UserPrincipal dto) {
        Post post = postRepository.save(Post.of(postRequest.title(), postRequest.content(), dto.toEntity()));

        if (postRequest.hashtags() != null) {
            for (String hashtagName : postRequest.hashtags()) {
                Hashtag hashtag = hashtagRepository.findByHashtagName(hashtagName)
                        .orElse(hashtagRepository.save(Hashtag.of(hashtagName)));
                post.getPostHashtags().add(postHashtagRepository.save(PostHashtag.of(post, hashtag)));
            }
        }

        if (multipartFiles != null)
            storageService.store(multipartFiles);

        return PostResponse.from(post);
    }

    // TODO: 추후에 유저 정보 넣을 것
    public PostResponse updatePost(Long postId, PostRequest postRequest, List<MultipartFile> multipartFiles, UserPrincipal dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.username());

        if (post.getUserAccount().equals(userAccount)) {
            if (postRequest.title() != null) post.setTitle(postRequest.title());
            if (postRequest.content() != null) post.setContent(postRequest.content());
            if (postRequest.hashtags() != null) {
                postHashtagRepository.deletePhByPostId(postId);
                postRepository.flush();

                // saveAll() 사용 코드
                List<Hashtag> newHashtags = new ArrayList<>();
                List<PostHashtag> newPostHashtags = new ArrayList<>();
                for (String hashtagName : postRequest.hashtags()) {
                    Hashtag hashtag = hashtagRepository.findByHashtagName(hashtagName)
                            .orElseGet(() -> {
                                Hashtag newHashtag = Hashtag.of(hashtagName);
                                newHashtags.add(newHashtag);
                                return newHashtag;
                            });
                    newPostHashtags.add(PostHashtag.of(post, hashtag));
                }
                hashtagRepository.saveAll(newHashtags);
                postHashtagRepository.saveAll(newPostHashtags);

                // save() 사용 코드
                /*for (String hashtagName : postRequest.hashtags()) {
                    Hashtag hashtag = hashtagRepository.findByHashtagName(hashtagName)
                            .orElseGet(() -> hashtagRepository.save(Hashtag.of(hashtagName)));
                    postHashtagRepository.save(PostHashtag.of(post, hashtag));
                }*/
            }
        }

        if (multipartFiles != null)
            storageService.store(multipartFiles);

        return PostResponse.from(post);
    }

    public void deletePost(Long postId, String username) {
        postRepository.deleteByIdAndUserAccount_UserId(postId, username);
    }

    public PostResponse registerPostLike(Long postId, UserPrincipal dto) {

        return null;
    }

    public PostResponse deletePostLike(Long postId, UserPrincipal dto) {

        return null;
    }

    //TODO: 이 로직은 필요가 없음, 이유 : 엔티티에 연관관계를 걸어 놓는 순간 포스트만 가져와도 관련된 정보를 가져올 때 JPA 알아서 조인해서 가져옴
    /*private Page<PostResponse> toResponses(Page<Post> posts, Pageable pageable) {
        List<PostResponse> postResponses = posts.stream()
                .map(post -> {
                    return PostResponse.from(post, getHashtagNames(post));
                })
                .toList();

        return new PageImpl<>(postResponses, pageable, postResponses.size());
    }

    private List<String> getHashtagNames(Post post) {
        return postHashtagRepository.findAllByPost_Id(post.getId())
                .stream()
                .map(postHashtag -> {
                    return hashtagRepository.getReferenceById(postHashtag.getHashtag().getId()).getHashtagName();
                })
                .toList();
    }*/

}
