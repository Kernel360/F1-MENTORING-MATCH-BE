package com.biengual.userapi.content.domain;

public interface ContentRepositoryCustom {

	String findTitleById(Long contentId);

	String findMongoIdByContentId(Long contentId);

	ContentType findContentTypeById(Long scriptIndex);

	boolean existsByUrl(String url);
}
