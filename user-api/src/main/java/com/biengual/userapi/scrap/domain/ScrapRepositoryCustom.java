package com.biengual.userapi.scrap.domain;

import java.util.List;

public interface ScrapRepositoryCustom {
	List<ScrapEntity> findAllByUserId(Long userId);

	void deleteScrap(Long userId, Long contentId);

	boolean existsScrap(Long userId, Long contentId);
}
