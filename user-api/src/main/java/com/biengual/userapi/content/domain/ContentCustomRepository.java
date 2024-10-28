package com.biengual.userapi.content.domain;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ContentCustomRepository {
	private final JPAQueryFactory queryFactory;

	public ContentType findContentTypeById(Long contentId) {
		QContentEntity contentEntity = QContentEntity.contentEntity;

		return queryFactory
			.select(contentEntity.contentType)
			.from(contentEntity)
			.where(contentEntity.id.eq(contentId))
			.fetchFirst();

	}

	public String findTitleById(Long contentId) {
		QContentEntity contentEntity = QContentEntity.contentEntity;

		return queryFactory
			.select(contentEntity.title)
			.from(contentEntity)
			.where(contentEntity.contentStatus.eq(ContentStatus.ACTIVATED).and(contentEntity.id.eq(contentId)))
			.fetchFirst();
	}

	public String findMongoIdByContentId(Long contentId) {
		QContentEntity contentEntity = QContentEntity.contentEntity;

		return queryFactory
			.select(contentEntity.mongoContentId)
			.from(contentEntity)
			.where(contentEntity.contentStatus.eq(ContentStatus.ACTIVATED).and(contentEntity.id.eq(contentId)))
			.fetchOne();
	}

	public boolean existsByUrl(String url) {
		QContentEntity contentEntity = QContentEntity.contentEntity;

		return queryFactory
			.from(contentEntity)
			.where(contentEntity.url.eq(url))
			.fetchFirst() != null;
	}
}
