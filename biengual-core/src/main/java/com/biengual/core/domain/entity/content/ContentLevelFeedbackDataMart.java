package com.biengual.core.domain.entity.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ContentLevelFeedback에 대한 집계를 보여주기 위한 DataMart 엔티티
 *
 * @author 문찬욱
 */
@Getter
@Entity
@Table(name = "content_level_feedback_data_mart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentLevelFeedbackDataMart {
    @Id
    @Column(name = "id")
    private Long contentId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long levelLowCount;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long levelMediumCount;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long levelHighCount;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long feedbackTotalCount;
}
