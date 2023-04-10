package com.project.simplecommunity.dto.reqeust;

import com.project.simplecommunity.domain.Post;
import com.project.simplecommunity.domain.UserAccount;

import java.util.List;

public record PostRequest(
        String title,
        String content,
        List<String> hashtags
) {
    public static PostRequest of(String title, String content, List<String> hashtags) {
        return new PostRequest(title, content, hashtags);
    }

    public static PostRequest of(String title, String content) {
        return new PostRequest(title, content, null);
    }

    public Post toEntity(UserAccount userAccount) {
        return Post.of(title, content, userAccount);
    }

}
