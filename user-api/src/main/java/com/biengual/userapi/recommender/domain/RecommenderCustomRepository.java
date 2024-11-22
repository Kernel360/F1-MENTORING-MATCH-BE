package com.biengual.userapi.recommender.domain;

import static com.biengual.core.domain.entity.learning.QCategoryLearningHistoryEntity.*;
import static com.biengual.core.domain.entity.recommender.QBookmarkRecommenderEntity.*;
import static com.biengual.core.domain.entity.recommender.QCategoryRecommenderEntity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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

    // 매개변수로 맏은 카테고리의 유사한 카테고리들을 찾기 위한 쿼리
    public List<Long> findSimilarCategories(List<Long> categoryIds) {
        // 1. CASE 문을 사용하여 리스트의 순서를 보장
        CaseBuilder.Cases<Integer, NumberExpression<Integer>> caseBuilder = new CaseBuilder()
            .when(categoryRecommenderEntity.categoryId.eq(categoryIds.get(0))).then(0);

        for (int i = 1; i < categoryIds.size() && i < 3; i++) {
            caseBuilder.when(categoryRecommenderEntity.categoryId.eq(categoryIds.get(i))).then(i);
        }
        NumberExpression<Integer> orderByClause = caseBuilder.otherwise(Expressions.asNumber(categoryIds.size()));

        // 2. QueryDSL 쿼리 실행 후 List<List<String>> 형태로 결과를 가져옴
        List<List<String>> similarCategoryIdsList = queryFactory
            .select(categoryRecommenderEntity.similarCategoryIds)
            .from(categoryRecommenderEntity)
            .where(categoryRecommenderEntity.categoryId.in(categoryIds))
            .orderBy(orderByClause.asc())
            .fetch();

        // 3. 중복을 피하기 위해 Set 사용
        Set<Long> uniqueSimilarCategoryIds = new LinkedHashSet<>();

        // 각 categoryId에 대해 similarCategoryIds 중 첫 번째로 categoryId와 다른 값을 선택
        for (Long categoryId : categoryIds) {
            similarCategoryIdsList.get(categoryIds.indexOf(categoryId))
                .stream()
                .map(Long::valueOf)  // String -> Long 변환
                .filter(similarId -> !categoryIds.contains(similarId))  // categoryId와 다른 값 선택
                .forEach(uniqueSimilarCategoryIds::add);  // Set 에 추가 (중복 자동 제거)

            // 3개를 찾으면 종료
            if (uniqueSimilarCategoryIds.size() >= 3) {
                break;
            }
        }

        // 4. 정확히 3개가 되도록 부족한 경우 추가 처리
        if (uniqueSimilarCategoryIds.size() < 3) {
            List<Long> additionalCategories = queryFactory
                .select(categoryRecommenderEntity.similarCategoryIds)
                .from(categoryRecommenderEntity)
                .where(categoryRecommenderEntity.categoryId.notIn(categoryIds)) // 기존 선택된 카테고리 제외
                .limit(3 - uniqueSimilarCategoryIds.size())  // 부족한 수만큼 추가로 가져옴
                .fetch()
                .stream()
                .flatMap(List::stream)
                .filter(str -> !str.isEmpty())
                .map(Long::valueOf)
                .toList();

            uniqueSimilarCategoryIds.addAll(additionalCategories);
        }

        // Set을 List로 변환하고 정확히 3개의 결과만 반환
        return uniqueSimilarCategoryIds.stream()
            .limit(3)  // 정확히 3개만 반환
            .collect(Collectors.toList());
    }

    // 유저가 처음 회원 가입을 해서 카테고리 관련 유저 정보가 없을 때 단순히 랜덤 카테고리를 가져오기 위한 쿼리
    public List<Long> findRandomCategories() {
        List<Long> categories = new ArrayList<>(
            queryFactory
                .select(categoryRecommenderEntity.similarCategoryIds)
                .from(categoryRecommenderEntity)
                .fetch()
                .stream()
                .flatMap(List::stream)
                .filter(str -> !str.isEmpty())
                .map(Long::valueOf)
                .toList()
        );

        Collections.shuffle(categories);

        return categories.subList(0, Math.min(3, categories.size()));
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
