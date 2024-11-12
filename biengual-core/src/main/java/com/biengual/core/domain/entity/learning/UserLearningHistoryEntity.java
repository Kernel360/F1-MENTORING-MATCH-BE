package com.biengual.core.domain.entity.learning;

import com.biengual.core.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_learning_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLearningHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long contentId;

    @Column(nullable = false, columnDefinition = "tinyint")
    private Integer learningRate;

    @Column(nullable = false)
    private LocalDateTime recentLearningTime;

    @Builder
    public UserLearningHistoryEntity(
        Long userId, Long contentId, Integer learningRate, LocalDateTime recentLearningTime
    ) {
        this.userId = userId;
        this.contentId = contentId;
        this.learningRate = learningRate;
        this.recentLearningTime = recentLearningTime;
    }

    public void record(Integer learningRate) {
        this.learningRate = learningRate;
        this.recentLearningTime = LocalDateTime.now();
    }
}
