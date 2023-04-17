package com.project.simplecommunity.repository;

import com.project.simplecommunity.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LikesRepository extends
        JpaRepository<Likes, Long>,
        QuerydslPredicateExecutor<Likes>
{

}
