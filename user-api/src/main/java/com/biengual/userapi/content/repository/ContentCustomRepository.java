package com.biengual.userapi.content.repository;

import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.QContentEntity;
import com.biengual.userapi.scrap.domain.QScrapEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContentCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 스크랩을 많이 한 컨텐츠를 조회하기 위한 쿼리
    public List<ContentInfo.PreviewContent> findContentsByScrapCount(Integer size) {
        QScrapEntity scrapEntity = QScrapEntity.scrapEntity;
        QContentEntity contentEntity = QContentEntity.contentEntity;

        // 커버링 인덱스
        List<Long> contentIds = queryFactory.select(scrapEntity.content.id)
            .from(scrapEntity)
            .groupBy(scrapEntity.content.id)
            .orderBy(scrapEntity.content.id.count().desc())
            .limit(size)
            .fetch();

        // contentIds가 비어있는 경우 빈 리스트 반환
        if (contentIds.isEmpty()) {
            return Collections.emptyList();
        }

        return queryFactory.select(
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
            .orderBy(coveringIndexOrder(contentEntity.id, contentIds))
            .fetch();
    }

    // Internal Methods=================================================================================================

    // 커버링 인덱스의 순서를 유지하기 위한 OrderSpecifier
    private OrderSpecifier<String> coveringIndexOrder(NumberPath<Long> idPath, List<Long> ids) {
        return Expressions.stringTemplate(
            "FIELD({0}, {1})",
            idPath,
            Expressions.constant(ids)
        ).asc();
    }
}
