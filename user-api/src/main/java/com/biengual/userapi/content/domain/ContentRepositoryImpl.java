package com.biengual.userapi.content.domain;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static com.biengual.userapi.content.domain.QContentEntity.contentEntity;

@Deprecated
public class ContentRepositoryImpl extends QuerydslRepositorySupport implements ContentRepositoryCustom {

	public ContentRepositoryImpl() {
		super(ContentEntity.class);
	}

	@Override
	public String findTitleById(Long contentId) {

		return from(contentEntity)
			.select(contentEntity.title)
			.where(contentEntity.contentStatus.eq(ContentStatus.ACTIVATED).and(contentEntity.id.eq(contentId)))
			.fetchFirst();
	}

	@Override
	public String findMongoIdByContentId(Long contentId) {
		return from(contentEntity)
			.select(contentEntity.mongoContentId)
			.where(contentEntity.contentStatus.eq(ContentStatus.ACTIVATED).and(contentEntity.id.eq(contentId)))
			.fetchOne();
	}

	@Override
	public ContentType findContentTypeById(Long contentId) {
		return from(contentEntity)
			.select(contentEntity.contentType)
			.where(contentEntity.id.eq(contentId))
			.fetchFirst();
	}

	@Override
	public boolean existsByUrl(String url) {
		return from(contentEntity)
			.where(contentEntity.url.eq(url))
			.fetchFirst() != null;
	}
}
