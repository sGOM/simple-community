package com.project.simplecommunity.dto.response.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SingleResponseDto<T> {
    private T data;
}

