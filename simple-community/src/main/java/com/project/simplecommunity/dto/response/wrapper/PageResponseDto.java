package com.project.simplecommunity.dto.response.wrapper;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponseDto<T> {
    private List<T> data;
    private PageInfo pageInfo;

    public PageResponseDto(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = PageInfo.of(page);
    }
}
