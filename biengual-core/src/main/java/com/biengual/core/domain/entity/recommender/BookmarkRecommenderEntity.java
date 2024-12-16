package com.biengual.core.domain.entity.recommender;

import java.time.LocalDateTime;

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
@Table(name = "bookmark_recommender")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkRecommenderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long contentId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long sentenceId;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String enDetail;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String koDetail;

    @Column(nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime startOfWeek;

    @Column(nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime endOfWeek;

    @Builder
    public BookmarkRecommenderEntity(
        Long id, Long contentId, Long sentenceId,
        String enDetail, String koDetail,
        LocalDateTime startOfWeek, LocalDateTime endOfWeek
    ) {
        this.id = id;
        this.contentId = contentId;
        this.sentenceId = sentenceId;
        this.enDetail = enDetail;
        this.koDetail = koDetail;
        this.startOfWeek = startOfWeek;
        this.endOfWeek = endOfWeek;
    }

    public static BookmarkRecommenderEntity createdByBookmark(
        Long scriptIndex, Long sentenceIndex,
        String enDetail, String koDetail,
        LocalDateTime startOfWeek, LocalDateTime endOfWeek
    ) {
        return BookmarkRecommenderEntity.builder()
            .contentId(scriptIndex)
            .sentenceId(sentenceIndex)
            .enDetail(enDetail)
            .koDetail(koDetail)
            .startOfWeek(startOfWeek)
            .endOfWeek(endOfWeek)
            .build();
    }
}