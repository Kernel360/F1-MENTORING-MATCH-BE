package com.biengual.userapi.category.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.enums.ContentType;
import com.biengual.userapi.category.domain.CategoryInfo;
import com.biengual.userapi.category.domain.CategoryReader;
import com.biengual.userapi.category.domain.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryReader categoryReader;

	// 모든 카테고리 조회
	@Override
	@Transactional(readOnly = true)
	public CategoryInfo.AllCategories getAllCategories() {
		return CategoryInfo.AllCategories.of(categoryReader.findAllCategories());
	}

    @Override
	@Transactional(readOnly = true)
    public CategoryInfo.AllCategories getCategoriesByContentType(ContentType contentType) {
        return CategoryInfo.AllCategories.of(categoryReader.findCategoriesByContentType(contentType));
    }
}
