package com.biengual.userapi.category.domain;

import java.util.List;

import com.biengual.core.enums.ContentType;

/**
 * Category 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface CategoryReader {
    List<CategoryInfo.Category> findAllCategories();

    List<CategoryInfo.Category> findCategoriesByContentType(ContentType contentType);
}
