package com.biengual.userapi.bookmark.domain;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biengual.userapi.core.entity.bookmark.BookmarkEntity;
import com.biengual.userapi.core.entity.bookmark.QBookmarkEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookmarkCustomRepository {
	private final JPAQueryFactory queryFactory;

	public void deleteBookmark(BookmarkCommand.Delete command) {
		QBookmarkEntity bookmarkEntity = QBookmarkEntity.bookmarkEntity;

		 queryFactory.delete(bookmarkEntity)
			.where(bookmarkEntity.userId.eq(command.userId()))
			.where(bookmarkEntity.id.eq(command.bookmarkId()))
			.execute();
	}

	public List<BookmarkEntity> findBookmarks(Long userId) {
		QBookmarkEntity bookmarkEntity = QBookmarkEntity.bookmarkEntity;

		return queryFactory.select(bookmarkEntity)
			.from(bookmarkEntity)
			.where(bookmarkEntity.userId.eq(userId))
			.orderBy(bookmarkEntity.createdAt.desc())
			.fetch();
	}

	public List<BookmarkEntity> findBookmarksByUserIdAndScriptIndex(Long userId, Long scriptIndex) {
		QBookmarkEntity bookmarkEntity = QBookmarkEntity.bookmarkEntity;

		return queryFactory.select(bookmarkEntity)
			.from(bookmarkEntity)
			.where(bookmarkEntity.userId.eq(userId))
			.where(bookmarkEntity.scriptIndex.eq(scriptIndex))
			.orderBy(bookmarkEntity.updatedAt.asc())
			.fetch();
	}

	public boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command) {
		QBookmarkEntity bookmarkEntity = QBookmarkEntity.bookmarkEntity;

		return queryFactory.from(bookmarkEntity)
			.where(bookmarkEntity.userId.eq(command.userId()))
			.where(bookmarkEntity.scriptIndex.eq(command.contentId()))
			.where(bookmarkEntity.sentenceIndex.eq(command.sentenceIndex()))
			.fetchFirst() != null;
	}
}
