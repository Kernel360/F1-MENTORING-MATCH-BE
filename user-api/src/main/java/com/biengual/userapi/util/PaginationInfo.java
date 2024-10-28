package com.biengual.userapi.util;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 공통적인 페이지네이션 정보를 담기 위한 클래스
 *
 * @author 문찬욱
 */
public record PaginationInfo<T> (
    Integer pageNumber,
    Integer pageSize,
    Integer totalPages,
    Long totalElements,
    List<T> contents
) {
        public static <T> PaginationInfo <T> from(Page<T> page) {
            return new PaginationInfo<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent()
            );
        }
}
