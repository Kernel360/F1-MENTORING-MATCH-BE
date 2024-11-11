package com.biengual.core.domain.entity.categorylearninghistory;

import com.biengual.core.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 학습 완료 기준을 만족한 카테고리별 학습 내역을 저장하기 위한 엔티티 클래스
 *
 * @author 문찬욱
 */
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
