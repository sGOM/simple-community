package com.project.simplecommunity.repository.querydsl;

import com.project.simplecommunity.domain.Post;
import com.project.simplecommunity.domain.QHashtag;
import com.project.simplecommunity.domain.QPost;
import com.project.simplecommunity.domain.QPostHashtag;
import com.project.simplecommunity.domain.searchtype.SearchType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class PostRepositoryCustomImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {
    public PostRepositoryCustomImpl() {
        super(Post.class);
    }

    @Override
    public Page<Post> findBySearchTypeAndSearchKeyword(SearchType searchType, String searchKeyword, Pageable pageable) {
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        switch (searchType) {
            case TITLE -> builder.and(post.title.containsIgnoreCase(searchKeyword));
            case CONTENT -> builder.and(post.content.containsIgnoreCase(searchKeyword));
            case USERID -> builder.and(post.userAccount.userId.containsIgnoreCase(searchKeyword));
        }
        JPQLQuery<Post> query = from(post)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Post> posts = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(posts, pageable, query.fetchCount());
    }

    @Override
    public Page<Post> findByHashtagNames(List<String> hashtagNames, Pageable pageable) {
        QPost post = QPost.post;
        QHashtag hashtag = QHashtag.hashtag;
        QPostHashtag postHashtag = QPostHashtag.postHashtag;

        JPQLQuery<Post> query = from(post)
                .innerJoin(post.postHashtags, postHashtag)
                .where(hashtag.hashtagName.in(hashtagNames))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Post> posts = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(posts, pageable, query.fetchCount());
    }

}
