package com.biengual.userapi.repository;

import com.biengual.core.domain.entity.content.ContentEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PessimisticLockContentRepository extends JpaRepository<ContentEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ContentEntity> findById(Long contentId);
}
