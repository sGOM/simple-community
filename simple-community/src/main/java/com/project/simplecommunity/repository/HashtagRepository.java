package com.project.simplecommunity.repository;

import com.project.simplecommunity.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface HashtagRepository extends
        JpaRepository<Hashtag, Long>,
        QuerydslPredicateExecutor<Hashtag>
{
    Optional<Hashtag> findByHashtagName(String hashtagName);
}
