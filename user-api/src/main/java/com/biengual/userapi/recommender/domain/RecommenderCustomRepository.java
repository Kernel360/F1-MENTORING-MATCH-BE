package com.biengual.userapi.recommender.domain;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.biengual.core.domain.entity.learning.QCategoryLearningHistoryEntity.categoryLearningHistoryEntity;
import static com.biengual.core.domain.entity.recommender.QBookmarkRecommenderEntity.bookmarkRecommenderEntity;
import static com.biengual.core.domain.entity.recommender.QCategoryRecommenderEntity.categoryRecommenderEntity;

@Repository
@RequiredArgsConstructor
public class RecommenderCustomRepository {
    private final JPAQueryFactory queryFactory;

    // CategoryRecommenderEntity를 업데이트 하기 위한 쿼리
    public void updateCategoryRecommender() {
        // 1. 각 카테고리별로 해당 카테고리를 많이 학습한 유저들의 ID를 가져옴
        Map<Long, List<Long>> categoryToUserIdsMap = queryFactory
            .select(categoryLearningHistoryEntity.categoryId, categoryLearningHistoryEntity.userId)
            .from(categoryLearningHistoryEntity)
            .fetch()
            .stream()
            .collect(Collectors.groupingBy(
                tuple -> Optional.ofNullable(tuple.get(categoryLearningHistoryEntity.categoryId)).orElse(-1L),
                Collectors.mapping(
                    tuple -> Optional.ofNullable(tuple.get(categoryLearningHistoryEntity.userId)).orElse(-1L),
                    Collectors.toList()
                )
            ));

        // 2. 각 카테고리에 대해 해당 유저들이 많이 학습한 다른 카테고리 목록을 찾음
        for (Map.Entry<Long, List<Long>> entry : categoryToUserIdsMap.entrySet()) {
            Long categoryId = entry.getKey();
            List<Long> userIds = entry.getValue();

            // 해당 유저들이 학습한 다른 카테고리들을 조회
            List<String> similarCategoryIds = queryFactory
                .select(categoryLearningHistoryEntity.categoryId.stringValue())
                .from(categoryLearningHistoryEntity)
                .where(categoryLearningHistoryEntity.userId.in(userIds)
                    .and(categoryLearningHistoryEntity.categoryId.ne(categoryId)))
                .groupBy(categoryLearningHistoryEntity.categoryId)
                .orderBy(categoryLearningHistoryEntity.categoryId.count().desc()) // 많이 학습한 순으로 정렬
                .limit(3) // 최대 3개의 비슷한 카테고리 추천
                .fetch();

            // 3. CategoryRecommenderEntity의 similarCategoryIds를 업데이트
            queryFactory
                .update(categoryRecommenderEntity)
                .where(categoryRecommenderEntity.categoryId.eq(categoryId))
                .set(categoryRecommenderEntity.similarCategoryIds, similarCategoryIds)
                .execute();
        }
    }

    // 이번주 인기 북마크 조회
    public List<RecommenderInfo.PopularBookmark> findPopularBookmarks(
        LocalDateTime startOfWeek, LocalDateTime endOfWeek
    ) {
        return queryFactory
            .select(
                Projections.constructor(
                    RecommenderInfo.PopularBookmark.class,
                    bookmarkRecommenderEntity.enDetail,
                    bookmarkRecommenderEntity.koDetail,
                    bookmarkRecommenderEntity.contentId
                )
            )
            .from(bookmarkRecommenderEntity)
            .where(
                bookmarkRecommenderEntity.startOfWeek.eq(startOfWeek)
                    .and(bookmarkRecommenderEntity.endOfWeek.eq(endOfWeek))
            )
            .fetch();
    }
}
