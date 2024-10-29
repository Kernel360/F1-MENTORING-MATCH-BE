package com.biengual.userapi.category.infrastructure;

import com.biengual.userapi.core.annotation.DataProvider;
import com.biengual.userapi.category.domain.CategoryInfo;
import com.biengual.userapi.category.domain.CategoryReader;
import com.biengual.userapi.category.domain.CategoryCustomRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DataProvider
@RequiredArgsConstructor
public class CategoryReaderImpl implements CategoryReader {
    private final CategoryCustomRepository categoryCustomRepository;

    // 모든 카테고리 조회
    @Override
    public List<CategoryInfo.Category> findAllCategories() {
        return categoryCustomRepository.findAllCategories();
    }
}
