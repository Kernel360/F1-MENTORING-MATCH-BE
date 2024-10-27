package com.biengual.userapi.content.repository;

import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentStatus;
import com.biengual.userapi.content.domain.ContentType;
import com.biengual.userapi.content.domain.QContentEntity;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.scrap.domain.QScrapEntity;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

import static com.biengual.userapi.message.error.code.ContentErrorCode.CONTENT_SORT_COL_NOT_FOUND;

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

    // 검색 조건에 맞는 컨텐츠를 조회하기 위한 쿼리
    public Page<ContentInfo.PreviewContent> findPreviewPageBySearch(Pageable pageable, String keyword) {
        QContentEntity contentEntity = QContentEntity.contentEntity;

        BooleanExpression predicate = getSearchPredicate(keyword, contentEntity);

        return findPreviewPage(pageable, predicate, contentEntity);
    }

    // 컨텐츠 뷰 페이지 조회하기 위한 쿼리
    public Page<ContentInfo.ViewContent> findViewPageByContentTypeAndCategoryId(
        Pageable pageable, ContentType contentType, Long categoryId
    ) {
        QContentEntity contentEntity = QContentEntity.contentEntity;

        BooleanExpression predicate = getViewPredicate(contentType, categoryId, contentEntity);

        List<OrderSpecifier<?>> orderSpecifiers = getPageOrderSpecifiers(pageable);

        return findViewPage(pageable, predicate, orderSpecifiers, contentEntity);
    }

    // 컨텐츠 프리뷰 조회하기 위한 쿼리
    public List<ContentInfo.PreviewContent> findPreviewBySizeAndSortAndContentType(
        Integer size, String sort, ContentType contentType
    ) {
        QContentEntity contentEntity = QContentEntity.contentEntity;

        BooleanExpression predicate = getPreviewPredicate(contentType, contentEntity);

        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(sort, contentEntity);

        return findPreview(size, predicate, orderSpecifiers, contentEntity);
    }

    // Internal Method =================================================================================================

    // TODO: Predicate를 사용하지 않는 경우에는 Override? 아니면 null로 입력?
    // Preview Page를 위한 공통 Pagination 쿼리
    private Page<ContentInfo.PreviewContent> findPreviewPage(Pageable pageable, Predicate predicate, QContentEntity contentEntity) {
        List<ContentInfo.PreviewContent> contents = queryFactory
            .select(
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
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(contentEntity.id.count())
            .from(contentEntity)
            .where(predicate);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    // TODO: Predicate를 사용하지 않는 경우에는 Override? 아니면 null로 입력?
    // View Page를 위한 공통 Pagination 쿼리
    private Page<ContentInfo.ViewContent> findViewPage(
        Pageable pageable, Predicate predicate, List<OrderSpecifier<?>> orderSpecifiers, QContentEntity contentEntity
    ) {
        List<ContentInfo.ViewContent> contents = queryFactory
            .select(
                Projections.constructor(
                    ContentInfo.ViewContent.class,
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
            .where(predicate)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(contentEntity.id.count())
            .from(contentEntity)
            .where(predicate);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    // TODO: Predicate를 사용하지 않는 경우에는 Override? 아니면 null로 입력?
    // Preview를 위한 공통 쿼리
    private List<ContentInfo.PreviewContent> findPreview(
        Integer size, Predicate predicate, List<OrderSpecifier<?>> orderSpecifiers, QContentEntity contentEntity
    ) {
        return queryFactory
            .select(
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
            .where(predicate)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
            .limit(size)
            .fetch();
    }

    // Public API에서 사용하는 공통 Predicate
    private BooleanExpression getPublicContentsPredicate(QContentEntity contentEntity) {
        return contentEntity.contentStatus.eq(ContentStatus.ACTIVATED);
    }

    // keyword 검색 비지니스 로직
    private List<String> splitAndLimitWords(String keyword) {
        return Arrays.stream(keyword.strip().replace(',', ' ').split("\\s+"))
            .limit(10)
            .toList();
    }

    // 각 word에 대한 Predicate
    private BooleanExpression getWordPredicate(String word, QContentEntity contentEntity) {
        return contentEntity.title.containsIgnoreCase(word)
            .or(contentEntity.preScripts.containsIgnoreCase(word));
    }

    // 최종 검색 Predicate
    private BooleanExpression getSearchPredicate(String keyword, QContentEntity contentEntity) {
        BooleanExpression baseExpression = getPublicContentsPredicate(contentEntity);

        List<String> selectedSearchWords = splitAndLimitWords(keyword);

        BooleanExpression searchExpression = selectedSearchWords.stream()
            .map(word -> getWordPredicate(word, contentEntity))
            .reduce(BooleanExpression::or)
            .orElse(null);

        return baseExpression.and(searchExpression);
    }

    // 컨텐츠 뷰 Predicate
    private BooleanExpression getViewPredicate(ContentType contentType, Long categoryId, QContentEntity contentEntity) {
        BooleanExpression baseExpression = getPublicContentsPredicate(contentEntity);

        BooleanExpression viewExpression = contentEntity.contentType.eq(contentType)
            .and(categoryId != null ? contentEntity.category.id.eq(categoryId) : null);

        return baseExpression.and(viewExpression);
    }

    // 컨텐츠 프리뷰 Predicate
    private BooleanExpression getPreviewPredicate(ContentType contentType, QContentEntity contentEntity) {
        BooleanExpression baseExpression = getPublicContentsPredicate(contentEntity);

        BooleanExpression previewExpression = contentEntity.contentType.eq(contentType);

        return baseExpression.and(previewExpression);
    }

    // Pageable OrderSpecifiers
    private List<OrderSpecifier<?>> getPageOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(QContentEntity.class, "contentEntity");
            orderSpecifiers.add(new OrderSpecifier(
                order.isAscending() ? Order.ASC : Order.DESC,
                pathBuilder.get(order.getProperty())
            ));
        }

        return orderSpecifiers;
    }

    // TODO: 정렬 가능 필드 범위가 정해지면 Dto 단계에서 검증할 것
    // OrderSpecifiers
    private List<OrderSpecifier<?>> getOrderSpecifiers(String sort, QContentEntity contentEntity) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        Field field;
        try {
            field = contentEntity.getClass().getField(sort);
        } catch (NoSuchFieldException e) {
            throw new CommonException(CONTENT_SORT_COL_NOT_FOUND);
        }

        Path<Object> path = Expressions.path(Object.class, contentEntity, field.getName());

        orderSpecifiers.add(new OrderSpecifier(Order.DESC, path));

        return orderSpecifiers;
    }
}
