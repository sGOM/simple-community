package com.project.simplecommunity.repository;

import com.project.simplecommunity.domain.Post;
import com.project.simplecommunity.domain.QPost;
import com.project.simplecommunity.repository.querydsl.PostRepositoryCustom;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface PostRepository extends
        JpaRepository<Post, Long>,
        QuerydslPredicateExecutor<Post>,
        QuerydslBinderCustomizer<QPost>,
        PostRepositoryCustom
{
    void deleteByIdAndUserAccount_UserId(Long postId, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QPost root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.createdBy);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}
