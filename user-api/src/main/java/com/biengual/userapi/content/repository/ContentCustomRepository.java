package com.biengual.userapi.content.repository;

import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentStatus;
import com.biengual.userapi.content.domain.QContentEntity;
import com.biengual.userapi.scrap.domain.QScrapEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ContentCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 스크랩을 많이 한 컨텐츠를 조회하기 위한 쿼리
    public List<ContentInfo.PreviewContent> findContentsByScrapCount(Integer size) {
        QScrapEntity scrapEntity = QScrapEntity.scrapEntity;
        QContentEntity contentEntity = QContentEntity.contentEntity;

        // 정렬된 커버링 인덱스
        List<Long> contentIds = queryFactory.select(scrapEntity.content.id)
            .from(scrapEntity)
            .where(scrapEntity.content.contentStatus.eq(ContentStatus.ACTIVATED))
            .groupBy(scrapEntity.content.id)
            .orderBy(scrapEntity.id.count().desc())
            .limit(size)
            .fetch();

        // contentIds가 비어있는 경우 빈 리스트 반환
        if (contentIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<ContentInfo.PreviewContent> unalignedScrapPreview = queryFactory.select(
                Projections.constructor(
                    ContentInfo.PreviewContent.class,
                    contentEntity.id,
                    contentEntity.title,
                    contentEntity.url,
                    contentEntity.contentType,
                    contentEntity.preScripts,
                    contentEntity.category.name,
                    contentEntity.hits
                )
            )
            .from(contentEntity)
            .where(contentEntity.id.in(contentIds))
            .fetch();

        // TODO: DB에서 정렬된 채로 가져올 수 있는 방법이 있다면?
        // 정렬된 커버링 인덱스의 순서에 따라 정렬
        return contentIds.stream()
            .map(id -> unalignedScrapPreview.stream()
                .filter(content -> content.contentId().equals(id))
                .findFirst()
                .orElse(null))
            .filter(Objects::nonNull)
            .toList();
    }
}
