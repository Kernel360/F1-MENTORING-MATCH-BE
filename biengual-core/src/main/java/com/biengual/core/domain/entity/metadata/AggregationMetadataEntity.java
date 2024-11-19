package com.biengual.core.domain.entity.metadata;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.enums.IntervalType;
import com.biengual.core.response.error.exception.CommonException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.biengual.core.constant.ServiceConstant.BIENGUAL_SERVICE_START_TIME;
import static com.biengual.core.constant.ServiceConstant.CONTENT_LEVEL_FEEDBACK_HISTORY_TABLE;
import static com.biengual.core.response.error.code.MetadataErrorCode.NOT_AGGREGATION_TABLE;

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
    private Integer intervalNumber;

    @Builder
    public AggregationMetadataEntity(
        Long id, String tableName, LocalDateTime aggregateEndTime, IntervalType intervalType, Integer intervalNumber
    ) {
        this.id = id;
        this.tableName = tableName;
        this.aggregateEndTime = aggregateEndTime;
        this.intervalType = intervalType;
        this.intervalNumber = intervalNumber;
    }

    // TODO: REST API가 아닌 백엔드 내부 로직에서 발생하는 예외인데, HttpStatus가 필요한지?
    // Table Name에 맞는 AggregationMetadataEntity를 생성하는 메서드
    public static AggregationMetadataEntity createEntityByTableName(String tableName) {
        switch (tableName) {
            case CONTENT_LEVEL_FEEDBACK_HISTORY_TABLE:
                return createEntity(tableName, IntervalType.ALL, -1);
            default:
                throw new CommonException(NOT_AGGREGATION_TABLE);
        }
    }

    public void updateAggregateEndTime(LocalDateTime aggregateEndTime) {
        this.aggregateEndTime = aggregateEndTime;
    }

    // Internal Method =================================================================================================

    private static AggregationMetadataEntity createEntity(
        String tableName, IntervalType intervalType, Integer intervalNumber
    ) {
        return AggregationMetadataEntity.builder()
            .tableName(tableName)
            .aggregateEndTime(BIENGUAL_SERVICE_START_TIME)
            .intervalType(intervalType)
            .intervalNumber(intervalNumber)
            .build();
    }
}
