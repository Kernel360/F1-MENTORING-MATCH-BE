package com.biengual.core.domain.entity.content;

import com.biengual.core.enums.ContentLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.biengual.core.constant.ServiceConstant.CONTENT_LEVEL_FEEDBACK_HISTORY_TABLE;

@Getter
@Entity
@Table(name = CONTENT_LEVEL_FEEDBACK_HISTORY_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentLevelFeedbackHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long contentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentLevel contentLevel;

    @Column(nullable = false, columnDefinition = "datetime(6)")
    private LocalDateTime feedbackTime;

    @Builder
    public ContentLevelFeedbackHistoryEntity(
        Long id, Long userId, Long contentId, ContentLevel contentLevel, LocalDateTime feedbackTime
    ) {
        this.id = id;
        this.userId = userId;
        this.contentId = contentId;
        this.contentLevel = contentLevel;
        this.feedbackTime = feedbackTime;
    }

    public static ContentLevelFeedbackHistoryEntity createEntity(
        Long userId, Long contentId, ContentLevel contentLevel
    ) {
        return ContentLevelFeedbackHistoryEntity.builder()
            .userId(userId)
            .contentId(contentId)
            .contentLevel(contentLevel)
            .feedbackTime(LocalDateTime.now())
            .build();
    }
}
