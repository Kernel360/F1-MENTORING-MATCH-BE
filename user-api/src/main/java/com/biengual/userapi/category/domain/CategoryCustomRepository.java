package com.biengual.userapi.category.domain;



import static com.biengual.core.domain.entity.category.QCategoryEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 서비스에 등록된 모든 카테고리를 조회하기 위한 쿼리
    public List<CategoryInfo.Category> findAllCategories() {
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

    public List<Long> findAllCategoryIds(){
        return queryFactory
            .select(categoryEntity.id)
            .from(categoryEntity)
            .fetch();
    }

}
