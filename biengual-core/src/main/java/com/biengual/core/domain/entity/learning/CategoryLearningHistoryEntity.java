package com.biengual.core.domain.entity.learning;

import java.time.LocalDateTime;

import com.biengual.core.domain.entity.BaseEntity;

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
    private Long contentId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long categoryId;

    @Column(nullable = false)
    private LocalDateTime learningTime;

    @Builder
    public CategoryLearningHistoryEntity(
        Long id, Long userId, Long contentId, Long categoryId, LocalDateTime learningTime
    ) {
        this.id = id;
        this.userId = userId;
        this.contentId = contentId;
        this.categoryId = categoryId;
        this.learningTime = learningTime;
    }
}
