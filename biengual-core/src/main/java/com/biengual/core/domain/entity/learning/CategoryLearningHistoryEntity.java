package com.biengual.core.domain.entity.learning;

import com.biengual.core.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "category_learning_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryLearningHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long categoryId;

    @Column(nullable = false)
    private LocalDateTime learningTime;

    @Builder
    public CategoryLearningHistoryEntity(
        Long id, Long userId, Long categoryId, LocalDateTime learningTime
    ) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.learningTime = learningTime;
    }
}
