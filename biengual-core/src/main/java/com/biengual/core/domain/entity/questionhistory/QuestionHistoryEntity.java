package com.biengual.core.domain.entity.questionhistory;

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

/**
 * 유저가 정답 요청한 문제를 기록하기 위한 엔티티
 */
@Entity
@Table(name = "question_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String questionId;

    @Column(nullable = false, columnDefinition = "boolean")
    private Boolean firstTry;   // 첫 시도 이후 변경 없음

    @Column(nullable = false, columnDefinition = "boolean")
    private Boolean finalTry;   // 최종 정답 이후 변경 없음 (T->F 되는 케이스 없음)

    @Column(nullable = false, columnDefinition = "bigint")
    private Long count;         // 모든 시도에 대해 업데이트

    @Builder
    public QuestionHistoryEntity(
        Long id, Long userId, String questionId, Boolean firstTry, Boolean finalTry, Long count
    ) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.firstTry = firstTry;
        this.finalTry = finalTry;
        this.count = count;
    }

    public static QuestionHistoryEntity createQuestionHistory(Long userId, String questionId, Boolean firstTry) {
        return QuestionHistoryEntity.builder()
            .userId(userId)
            .questionId(questionId)
            .firstTry(firstTry)
            .finalTry(firstTry)
            .count(1L)
            .build();
    }

    public void updateQuestionHistory(Boolean isCorrect) {
        if((this.firstTry).equals(Boolean.FALSE)){
            this.finalTry = isCorrect;
        }
    }
}
