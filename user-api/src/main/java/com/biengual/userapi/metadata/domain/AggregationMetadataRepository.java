package com.biengual.userapi.metadata.domain;

import com.biengual.core.domain.entity.metadata.AggregationMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * AggregationMetadataEntity Repository 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface AggregationMetadataRepository extends JpaRepository<AggregationMetadataEntity, Long> {
    Optional<AggregationMetadataEntity> findByTableName(String tableName);
}
