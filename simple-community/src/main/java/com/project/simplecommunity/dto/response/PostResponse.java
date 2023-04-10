package com.project.simplecommunity.dto.response;

import com.project.simplecommunity.domain.Hashtag;
import com.project.simplecommunity.domain.Post;
import com.project.simplecommunity.domain.PostHashtag;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long postId,
        String title,
        String content,
        String username,
        List<String> hashtags,
        LocalDateTime lastModifiedAt
) {
    public static PostResponse of(Long postId, String title, String content, String username, List<String> hashtags, LocalDateTime lastModifiedAt) {
        return new PostResponse(postId, title, content, username, hashtags, lastModifiedAt);
    }

    public static PostResponse from(Post entity) {
        List<String> hashtags = entity.getPostHashtags().stream()
                .map(PostHashtag::getHashtag)
                .map(Hashtag::getHashtagName)
                .toList();

        return PostResponse.of(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getUserAccount().getUserId(),
                hashtags,
                entity.getModifiedAt()
        );
    }
    public static PostResponse from(Post entity, List<String> hashtags) {
        return PostResponse.of(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getUserAccount().getUserId(),
                hashtags,
                entity.getModifiedAt()
        );
    }

}
