package com.biengual.core.domain.entity.pointdatamart;

import java.util.Optional;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.domain.entity.pointhistory.PointHistoryEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 총 포인트 획득, 총 포인트 사용, 마지막 업데이트 등을 빠르게 조회하기 위한 엔티티
 *
 * @author 김영래
 */
@Entity
@Getter
@Table(name = "point_data_mart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointDataMart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long totalPointsEarned;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long totalPointsSpent;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long lastProcessedBalance;

    @Builder
    private PointDataMart(
        Long userId, Long totalPointsEarned, Long totalPointsSpent, Long lastProcessedBalance
    ) {
        this.userId = userId;
        this.totalPointsEarned = Optional.ofNullable(totalPointsEarned).orElse(0L);
        this.totalPointsSpent = Optional.ofNullable(totalPointsSpent).orElse(0L);
        this.lastProcessedBalance = Optional.ofNullable(lastProcessedBalance).orElse(0L);
    }

    public static PointDataMart createPointDataMart(Long userId) {
        return PointDataMart.builder()
            .userId(userId)
            .build();
    }

    public void updateByPointHistory(PointHistoryEntity pointHistory) {
        this.userId = pointHistory.getUserId();
        if (pointHistory.getPointChange() > 0) {
            this.totalPointsEarned = (this.getTotalPointsEarned() + pointHistory.getPointChange());
        } else {
            this.totalPointsSpent = (this.getTotalPointsSpent() - pointHistory.getPointChange());
        }
        this.lastProcessedBalance = pointHistory.getPointBalance();
    }
}