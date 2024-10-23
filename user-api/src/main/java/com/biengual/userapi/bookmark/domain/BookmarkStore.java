package com.biengual.userapi.bookmark.domain;

public interface BookmarkStore {
	void deleteBookmark(Long userId, Long bookmarkId);

	void saveBookmark(BookmarkCommand.Create command, String detail, Double startTime);

	BookmarkInfo.Position updateBookmark(BookmarkCommand.Update command);
}