package com.biengual.userapi.scrap.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.scrap.ScrapEntity;

public interface ScrapRepository extends JpaRepository<ScrapEntity, Long> {
}
