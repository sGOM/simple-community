package com.project.simplecommunity.dto.response.wrapper;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageInfo {
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

    private PageInfo(Page page) {
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public static PageInfo of(Page page) {
        return new PageInfo(page);
    }
}
