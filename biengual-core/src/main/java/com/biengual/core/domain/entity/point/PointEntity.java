package com.biengual.core.domain.entity.point;

import static com.biengual.core.constant.BadRequestMessageConstant.*;
import static com.biengual.core.constant.RestrictionConstant.*;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.enums.PointReason;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저의 현재 총 포인트 저장 목적 엔티티
 *
 * @author 김영래
 */
@Entity
@Getter
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointEntity extends BaseEntity {
    @Id
    @Column(name = "id")
    private Long userId;

    @Column(name = "current_point", nullable = false, columnDefinition = "bigint")
    @Min(value = MIN_POINT_AMOUNT_LIMIT, message = MIN_POINT_AMOUNT_ERROR_MESSAGE)
    private Long currentPoint;

    @Builder
    public PointEntity(Long userId, PointReason pointReason) {
        this.userId = userId;
        this.currentPoint = (long)pointReason.getValue();
    }

    public static PointEntity createByUserId(Long userId) {
        return PointEntity.builder()
            .userId(userId)
            .pointReason(PointReason.FIRST_SIGN_UP)
            .build();
    }

}
