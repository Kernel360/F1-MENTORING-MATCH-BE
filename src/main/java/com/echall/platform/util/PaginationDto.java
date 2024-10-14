package com.echall.platform.util;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginationDto<T>(
    Integer pageNumber,
    Integer pageSize,
    Integer totalPages,
    Long totalElements,
    List<T> contents
) {
    public static <T> PaginationDto<T> from(Page<?> page, List<T> contents) {
        return new PaginationDto<>(
            page.getNumber(),
            page.getSize(),
            page.getTotalPages(),
            page.getTotalElements(),
            contents
        );
    }
}
