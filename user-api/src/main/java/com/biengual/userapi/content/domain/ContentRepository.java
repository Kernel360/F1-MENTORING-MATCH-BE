package com.biengual.userapi.content.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentRepository extends JpaRepository<ContentEntity, Long>, ContentRepositoryCustom {
    Optional<ContentEntity> findByIdAndContentStatus(Long contentId, ContentStatus contentStatus);
}
