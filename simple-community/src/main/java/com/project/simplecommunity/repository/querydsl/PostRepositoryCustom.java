package com.project.simplecommunity.repository.querydsl;

import com.project.simplecommunity.domain.Post;
import com.project.simplecommunity.domain.searchtype.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    Page<Post> findBySearchTypeAndSearchKeyword(SearchType searchType, String searchKeyword, Pageable pageable);
    Page<Post> findByHashtagNames(List<String> hashtagNames, Pageable pageable);
}
