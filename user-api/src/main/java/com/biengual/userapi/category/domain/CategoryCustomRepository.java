package com.biengual.userapi.category.domain;

import com.biengual.userapi.core.entity.category.QCategoryEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepository {
	private final JPAQueryFactory queryFactory;

	// 서비스에 등록된 모든 카테고리를 조회하기 위한 쿼리
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
