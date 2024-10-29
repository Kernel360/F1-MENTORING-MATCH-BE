package com.biengual.userapi.bookmark.domain;


import static com.biengual.core.domain.entity.bookmark.QBookmarkEntity.*;

import java.util.List;

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

    public List<BookmarkEntity> findBookmarks(Long userId) {
        return queryFactory.select(bookmarkEntity)
            .from(bookmarkEntity)
            .where(bookmarkEntity.userId.eq(userId))
            .orderBy(bookmarkEntity.createdAt.desc())
            .fetch();
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
}
