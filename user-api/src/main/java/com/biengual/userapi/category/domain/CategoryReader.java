package com.biengual.userapi.category.domain;

import java.util.List;

/**
 * Category 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface CategoryReader {
    List<CategoryInfo.Category> findAllCategories();
}
