package com.biengual.userapi.scrap.repository.custom;

import static com.biengual.userapi.message.error.code.ScrapErrorCode.*;
import static com.biengual.userapi.scrap.domain.entity.QScrapEntity.*;
import static com.biengual.userapi.user.domain.entity.QUserEntity.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.scrap.domain.entity.ScrapEntity;

public class ScrapRepositoryImpl extends QuerydslRepositorySupport implements ScrapRepositoryCustom {
	public ScrapRepositoryImpl() {
		super(ScrapEntity.class);
	}

	@Override
	public List<ScrapEntity> findAllByUserId(Long userId) {
		return from(userEntity)
			.join(userEntity.scraps, scrapEntity)
			.select(scrapEntity)
			.orderBy(scrapEntity.createdAt.desc())
			.where(userEntity.id.eq(userId))
			.fetch();
	}

	@Override
	public void deleteScrap(Long userId, Long contentId) {
		Optional.ofNullable(from(userEntity)
				.join(userEntity.scraps, scrapEntity)
				.select(scrapEntity)
				.where(userEntity.id.eq(userId))
				.where(scrapEntity.content.id.eq(contentId))
				.fetchOne())
			.orElseThrow(() -> new CommonException(SCRAP_NOT_FOUND));
		delete(scrapEntity)
			.where(scrapEntity.content.id.eq(contentId))
			.execute();

	}

	@Override
	public boolean existsScrap(Long userId, Long contentId) {
		return from(userEntity)
			.join(userEntity.scraps, scrapEntity)
			.select(scrapEntity)
			.where(userEntity.id.eq(userId))
			.where(scrapEntity.content.id.eq(contentId))
			.fetchFirst() != null;
	}
}
