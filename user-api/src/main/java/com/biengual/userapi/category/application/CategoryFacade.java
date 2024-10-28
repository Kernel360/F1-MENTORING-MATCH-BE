package com.biengual.userapi.category.application;

import com.biengual.userapi.annotation.Facade;
import com.biengual.userapi.category.domain.CategoryInfo;
import com.biengual.userapi.category.domain.CategoryService;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class CategoryFacade {
    private final CategoryService categoryService;

    public CategoryInfo.AllCategories getAllCategories() {
        return categoryService.getAllCategories();
    }
}
