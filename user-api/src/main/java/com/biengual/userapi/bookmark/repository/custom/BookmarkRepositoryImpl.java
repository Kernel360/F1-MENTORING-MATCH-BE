package com.biengual.userapi.bookmark.repository.custom;

import com.biengual.userapi.bookmark.domain.dto.BookmarkRequestDto;
import com.biengual.userapi.bookmark.domain.entity.BookmarkEntity;
import com.biengual.userapi.message.error.exception.CommonException;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.biengual.userapi.bookmark.domain.entity.QBookmarkEntity.bookmarkEntity;
import static com.biengual.userapi.message.error.code.BookmarkErrorCode.BOOKMARK_NOT_FOUND;
import static com.biengual.userapi.user.domain.entity.QUserEntity.userEntity;

public class BookmarkRepositoryImpl extends QuerydslRepositorySupport implements BookmarkRepositoryCustom {

	public BookmarkRepositoryImpl() {
		super(BookmarkEntity.class);
	}

	@Override
	public Long deleteBookmark(Long userId, Long bookmarkId) {
		BookmarkEntity bookmark = Optional.ofNullable(from(userEntity)
				.join(userEntity.bookmarks, bookmarkEntity)
				.select(bookmarkEntity)
				.where(userEntity.id.eq(userId))
				.where(bookmarkEntity.id.eq(bookmarkId))
				.fetchOne())
			.orElseThrow(() -> new CommonException(BOOKMARK_NOT_FOUND));

		delete(bookmarkEntity)
			.where(bookmarkEntity.id.eq(bookmarkId))
			.execute();

		return bookmark.getId();
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
	public boolean isBookmarkAlreadyPresent(Long scriptIndex, BookmarkRequestDto.BookmarkCreateRequest request, Long userId) {
		return from(userEntity)
			.leftJoin(userEntity.bookmarks, bookmarkEntity)
			.fetchJoin()
			.where(bookmarkBooleanExpression(scriptIndex, request, userId))
			.fetchFirst() != null;

	}

	private BooleanExpression bookmarkBooleanExpression(
		Long scriptIndex, BookmarkRequestDto.BookmarkCreateRequest request, Long userId
	) {
		BooleanExpression booleanExpression = userEntity.id.eq(userId)
			.and(bookmarkEntity.scriptIndex.eq(scriptIndex))
			.and(bookmarkEntity.sentenceIndex.eq(request.sentenceIndex()));

		if (request.wordIndex() != null) {
			booleanExpression.and(bookmarkEntity.wordIndex.eq(request.wordIndex()));
		}

		return booleanExpression;
	}
}
