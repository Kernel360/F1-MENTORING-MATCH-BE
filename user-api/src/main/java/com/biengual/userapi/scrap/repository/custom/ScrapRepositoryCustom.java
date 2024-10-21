package com.biengual.userapi.scrap.repository.custom;

import java.util.List;

import com.biengual.userapi.scrap.domain.entity.ScrapEntity;

public interface ScrapRepositoryCustom {
	List<ScrapEntity> findAllByUserId(Long userId);

	void deleteScrap(Long userId, Long contentId);

	boolean existsScrap(Long userId, Long contentId);
}
