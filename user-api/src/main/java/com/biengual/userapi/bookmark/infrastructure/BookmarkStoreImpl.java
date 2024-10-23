package com.biengual.userapi.bookmark.infrastructure;

import static com.biengual.userapi.message.error.code.BookmarkErrorCode.*;
import static com.biengual.userapi.message.error.code.UserErrorCode.*;

import java.util.Objects;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkEntity;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.bookmark.domain.BookmarkRepository;
import com.biengual.userapi.bookmark.domain.BookmarkStore;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.user.domain.entity.UserEntity;
import com.biengual.userapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class BookmarkStoreImpl implements BookmarkStore {
	private final BookmarkRepository bookmarkRepository;
	private final UserRepository userRepository;

	@Override
	public void deleteBookmark(Long userId, Long bookmarkId) {
		bookmarkRepository.deleteBookmark(userId, bookmarkId);
	}

	@Override
	public void saveBookmark(BookmarkCommand.Create command, String detail, Double startTime) {
		UserEntity user = userRepository.findById(command.userId())
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		BookmarkEntity bookmark = command.toEntity(detail, startTime);
		bookmarkRepository.save(bookmark);
		user.updateUserBookmark(bookmark);
	}

	@Override
	public BookmarkInfo.Position updateBookmark(BookmarkCommand.Update command) {
		UserEntity user = userRepository.findById(command.userId())
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		BookmarkEntity bookmark = user.getBookmarks()
			.stream()
			.filter(bookmark1 -> Objects.equals(bookmark1.getId(), command.bookmarkId()))
			.findFirst()
			.orElseThrow(() -> new CommonException(BOOKMARK_NOT_FOUND));

		bookmark.updateDescription(command.description());

		return BookmarkInfo.Position.of(bookmark);
	}
}
