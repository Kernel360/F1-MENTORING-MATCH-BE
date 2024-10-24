package com.biengual.userapi.scrap.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<ScrapEntity, Long>, ScrapRepositoryCustom {
	Long countByContentId(Long content_id);
}
