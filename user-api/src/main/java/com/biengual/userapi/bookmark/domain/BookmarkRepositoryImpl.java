package com.biengual.userapi.bookmark.domain;

import com.biengual.userapi.message.error.exception.CommonException;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.biengual.userapi.bookmark.domain.QBookmarkEntity.bookmarkEntity;
import static com.biengual.userapi.message.error.code.BookmarkErrorCode.BOOKMARK_NOT_FOUND;
import static com.biengual.userapi.user.domain.QUserEntity.userEntity;

public class BookmarkRepositoryImpl extends QuerydslRepositorySupport implements BookmarkRepositoryCustom {

	public BookmarkRepositoryImpl() {
		super(BookmarkEntity.class);
	}

	@Override
	public void deleteBookmark(Long userId, Long bookmarkId) {
		Optional.ofNullable(from(userEntity)
				.join(userEntity.bookmarks, bookmarkEntity)
				.select(bookmarkEntity)
				.where(userEntity.id.eq(userId))
				.where(bookmarkEntity.id.eq(bookmarkId))
				.fetchOne())
			.orElseThrow(() -> new CommonException(BOOKMARK_NOT_FOUND));

		delete(bookmarkEntity)
			.where(bookmarkEntity.id.eq(bookmarkId))
			.execute();
	}

	@Override
	public List<BookmarkEntity> getAllBookmarks(Long userId) {
		return from(userEntity)
			.join(userEntity.bookmarks, bookmarkEntity)
			.select(bookmarkEntity)
			.where(userEntity.id.eq(userId))
			.orderBy(bookmarkEntity.createdAt.desc())
			.fetch();
	}

	@Override
	public boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command) {
		return from(userEntity)
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
