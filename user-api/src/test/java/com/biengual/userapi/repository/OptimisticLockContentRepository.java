package com.biengual.userapi.repository;

import com.biengual.core.domain.entity.content.ContentEntityWithVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptimisticLockContentRepository extends JpaRepository<ContentEntityWithVersion, Long> {
}
