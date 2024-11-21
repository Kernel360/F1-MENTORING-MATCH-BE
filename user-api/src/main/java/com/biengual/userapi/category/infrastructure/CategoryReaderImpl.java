package com.biengual.userapi.category.infrastructure;

import java.util.List;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.enums.ContentType;
import com.biengual.userapi.category.domain.CategoryCustomRepository;
import com.biengual.userapi.category.domain.CategoryInfo;
import com.biengual.userapi.category.domain.CategoryReader;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class CategoryReaderImpl implements CategoryReader {
    private final CategoryCustomRepository categoryCustomRepository;

    // 모든 카테고리 조회
    @Override
    public List<CategoryInfo.Category> findAllCategories() {
        return categoryCustomRepository.findAllCategories();
    }

    @Override
    public List<CategoryInfo.Category> findCategoriesByContentType(ContentType contentType) {
        return categoryCustomRepository.findCategoriesByContentType(contentType);
    }
}
