package com.biengual.userapi.category.repository;

import com.biengual.userapi.category.domain.CategoryInfo;

import com.biengual.userapi.category.domain.QCategoryEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<CategoryInfo.Category> findAllCategories() {
        QCategoryEntity categoryEntity = QCategoryEntity.categoryEntity;

        return queryFactory.select(
                Projections.constructor(
                    CategoryInfo.Category.class,
                    categoryEntity.id,
                    categoryEntity.name
                )
            )
            .from(categoryEntity)
            .fetch();
    }
}
