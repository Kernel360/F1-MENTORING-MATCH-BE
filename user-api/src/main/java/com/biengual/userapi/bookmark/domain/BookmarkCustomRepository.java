package com.biengual.userapi.bookmark.domain;

import static com.biengual.userapi.bookmark.domain.QBookmarkEntity.*;
import static com.biengual.userapi.user.domain.QUserEntity.*;

import java.util.List;

import com.biengual.userapi.annotation.DataProvider;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class BookmarkCustomRepository {
	private final JPAQueryFactory queryFactory;

	public void deleteBookmark(BookmarkCommand.Delete command) {
		queryFactory.delete(bookmarkEntity)
			.where(bookmarkEntity.id.eq(command.bookmarkId()))
			.execute();
	}

	public boolean isPresentBookmark(BookmarkCommand.Delete command) {
		return queryFactory.from(userEntity)
			.join(userEntity.bookmarks, bookmarkEntity)
			.where(userEntity.id.eq(command.userId()))
			.where(bookmarkEntity.id.eq(command.bookmarkId()))
			.fetchFirst() != null;
	}

	public List<BookmarkEntity> getAllBookmarks(Long userId) {
		return queryFactory.select(bookmarkEntity)
			.from(userEntity)
			.join(userEntity.bookmarks, bookmarkEntity)
			.where(userEntity.id.eq(userId))
			.orderBy(bookmarkEntity.createdAt.desc())
			.fetch();
	}

	public boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command) {
		return queryFactory.from(userEntity)
			.leftJoin(userEntity.bookmarks, bookmarkEntity)
			.where(bookmarkBooleanExpression(command))
			.fetchFirst() != null;
	}

	private BooleanExpression bookmarkBooleanExpression(BookmarkCommand.Create command) {
		return userEntity.id.eq(command.userId())
			.and(bookmarkEntity.scriptIndex.eq(command.contentId()))
			.and(bookmarkEntity.sentenceIndex.eq(command.sentenceIndex()));
	}
}
