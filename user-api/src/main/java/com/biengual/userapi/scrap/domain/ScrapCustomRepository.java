package com.biengual.userapi.scrap.domain;

import static com.biengual.userapi.message.error.code.ScrapErrorCode.*;
import static com.biengual.userapi.scrap.domain.QScrapEntity.*;
import static com.biengual.userapi.user.domain.QUserEntity.*;

import java.util.List;
import java.util.Optional;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.message.error.exception.CommonException;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class ScrapCustomRepository {
	private final JPAQueryFactory queryFactory;

	public List<ScrapEntity> findAllByUserId(Long userId) {
		return queryFactory.select(scrapEntity)
			.from(userEntity)
			.join(userEntity.scraps, scrapEntity)
			.orderBy(scrapEntity.createdAt.desc())
			.where(userEntity.id.eq(userId))
			.fetch();
	}

	public void deleteScrap(Long userId, Long contentId) {
		Optional.ofNullable(queryFactory.select(scrapEntity)
				.from(userEntity)
				.join(userEntity.scraps, scrapEntity)
				.where(userEntity.id.eq(userId))
				.where(scrapEntity.content.id.eq(contentId))
				.fetchOne())
			.orElseThrow(() -> new CommonException(SCRAP_NOT_FOUND));
		queryFactory.delete(scrapEntity)
			.where(scrapEntity.content.id.eq(contentId))
			.execute();

	}

	public boolean existsScrap(Long userId, Long contentId) {
		return queryFactory.select(scrapEntity)
			.from(userEntity)
			.join(userEntity.scraps, scrapEntity)
			.where(userEntity.id.eq(userId))
			.where(scrapEntity.content.id.eq(contentId))
			.fetchFirst() != null;
	}
}
