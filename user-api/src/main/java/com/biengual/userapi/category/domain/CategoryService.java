package com.biengual.userapi.category.domain;

import com.biengual.core.enums.ContentType;

/**
 * Category 도메인의 Service 계층의 인터페이스
 */
public interface CategoryService {
	CategoryInfo.AllCategories getAllCategories();

    CategoryInfo.AllCategories getCategoriesByContentType(ContentType contentType);
}