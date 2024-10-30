package com.biengual.userapi.bookmark.domain;


import static com.biengual.core.domain.entity.bookmark.QBookmarkEntity.*;
import static com.biengual.core.domain.entity.content.QContentEntity.contentEntity;

import java.util.List;

import com.querydsl.core.types.Projections;
import org.springframework.stereotype.Repository;

import com.biengual.core.domain.entity.bookmark.BookmarkEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookmarkCustomRepository {
    private final JPAQueryFactory queryFactory;

    public void deleteBookmark(BookmarkCommand.Delete command) {
        queryFactory.delete(bookmarkEntity)
            .where(bookmarkEntity.userId.eq(command.userId()))
            .where(bookmarkEntity.id.eq(command.bookmarkId()))
            .execute();
    }

    public List<BookmarkEntity> findBookmarksByUserIdAndScriptIndex(Long userId, Long scriptIndex) {
        return queryFactory.select(bookmarkEntity)
            .from(bookmarkEntity)
            .where(bookmarkEntity.userId.eq(userId))
            .where(bookmarkEntity.scriptIndex.eq(scriptIndex))
            .orderBy(bookmarkEntity.updatedAt.asc())
            .fetch();
    }

    public boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command) {
        return queryFactory.from(bookmarkEntity)
            .where(bookmarkEntity.userId.eq(command.userId()))
            .where(bookmarkEntity.scriptIndex.eq(command.contentId()))
            .where(bookmarkEntity.sentenceIndex.eq(command.sentenceIndex()))
            .fetchFirst() != null;
    }

    // 나의 전체 북마크 조회 기능을 위한 쿼리
    public List<BookmarkInfo.MyList> findBookmarkMyListByUserId(Long userId) {
        return queryFactory
            .select(
                Projections.constructor(
                    BookmarkInfo.MyList.class,
                    bookmarkEntity.id,
                    contentEntity.contentType,
                    bookmarkEntity.detail,
                    bookmarkEntity.description,
                    bookmarkEntity.scriptIndex,
                    contentEntity.title,
                    bookmarkEntity.createdAt,
                    bookmarkEntity.updatedAt,
                    contentEntity.contentStatus
                )
            )
            .from(bookmarkEntity)
            .leftJoin(contentEntity).on(bookmarkEntity.scriptIndex.eq(contentEntity.id))
            .where(bookmarkEntity.userId.eq(userId))
            .orderBy(bookmarkEntity.updatedAt.desc())
            .fetch();
    }
}
