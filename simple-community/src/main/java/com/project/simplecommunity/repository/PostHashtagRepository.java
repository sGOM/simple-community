package com.project.simplecommunity.repository;

import com.project.simplecommunity.domain.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostHashtagRepository extends
        JpaRepository<PostHashtag, Long>,
        QuerydslPredicateExecutor<PostHashtag>
{
    @Modifying
    @Query(value = "delete from PostHashtag ph where ph.post.id=:postId")
    void deletePhByPostId(@Param("postId") Long postId);

    List<PostHashtag> findAllByPost_Id(Long postId);
}
