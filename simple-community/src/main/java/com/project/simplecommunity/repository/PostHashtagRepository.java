package com.project.simplecommunity.repository;

import com.project.simplecommunity.domain.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PostHashtagRepository extends
        JpaRepository<PostHashtag, Long>,
        QuerydslPredicateExecutor<PostHashtag>
{
    void deleteAllByPost_Id(Long postId);
    List<PostHashtag> findAllByPost_Id(Long postId);
}
