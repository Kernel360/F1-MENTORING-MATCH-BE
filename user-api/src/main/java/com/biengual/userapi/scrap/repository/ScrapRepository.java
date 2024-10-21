package com.biengual.userapi.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.userapi.scrap.domain.entity.ScrapEntity;
import com.biengual.userapi.scrap.repository.custom.ScrapRepositoryCustom;

public interface ScrapRepository extends JpaRepository<ScrapEntity, Long>, ScrapRepositoryCustom {
	Long countByContentId(Long content_id);
}
