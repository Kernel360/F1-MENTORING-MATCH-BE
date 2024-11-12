package com.biengual.core.domain.entity.learning;

import com.biengual.core.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "learning_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long contentId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long categoryId;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal learningRate;

    @Column(nullable = false)
    private LocalDateTime learningTime;

    @Builder
    public LearningHistoryEntity(
        Long id, Long userId, Long contentId, Long categoryId,
        BigDecimal learningRate, LocalDateTime learningTime
    ) {
        this.id = id;
        this.userId = userId;
        this.contentId = contentId;
        this.categoryId = categoryId;
        this.learningRate = learningRate;
        this.learningTime = learningTime;
    }
}
