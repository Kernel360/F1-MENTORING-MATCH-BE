package com.biengual.userapi.scrap.domain;

import static com.biengual.userapi.scrap.domain.QScrapEntity.*;
import static com.biengual.userapi.user.domain.QUserEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
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

	public boolean deleteScrap(ScrapCommand.Delete command) {
		return queryFactory.delete(scrapEntity)
			.where(scrapEntity.userId.eq(command.userId()))
			.where(scrapEntity.content.id.eq(command.contentId()))
			.execute() > 0;

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
