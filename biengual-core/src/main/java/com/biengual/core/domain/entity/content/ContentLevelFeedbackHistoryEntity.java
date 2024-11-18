package com.biengual.core.domain.entity.content;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.enums.ContentLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "content_level_feedback_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentLevelFeedbackHistoryEntity extends BaseEntity {
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

    @Builder
    public ContentLevelFeedbackHistoryEntity(
        Long id, Long userId, Long contentId, ContentLevel contentLevel
    ) {
        this.id = id;
        this.userId = userId;
        this.contentId = contentId;
        this.contentLevel = contentLevel;
    }
}
