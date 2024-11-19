package com.biengual.core.domain.entity.metadata;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.enums.IntervalType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 집계 유형의 메타데이터를 저장하기 위한 엔티티
 *
 * @author 문찬욱
 */
@Getter
@Entity
@Table(name = "aggregation_metadata")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AggregationMetadataEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(100)")
    private String tableName;

    @Column(nullable = false, columnDefinition = "datetime(6)")
    private LocalDateTime aggregateEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IntervalType intervalType;

    @Column(nullable = false, columnDefinition = "tinyint")
    private Integer interval;
}
