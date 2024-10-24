package com.biengual.userapi.bookmark.domain;

import java.util.List;

public interface BookmarkRepositoryCustom {
	void deleteBookmark(Long userId, Long bookmarkId);

	List<BookmarkEntity> getAllBookmarks(Long userId);

	boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command);
}
