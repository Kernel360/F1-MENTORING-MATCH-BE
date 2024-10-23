package com.biengual.userapi.bookmark.domain;

public interface BookmarkStore {
	void deleteBookmark(Long userId, Long bookmarkId);

	BookmarkInfo.Create saveBookmark(BookmarkCommand.Create command, String detail, Double startTime);

	BookmarkInfo.Content updateBookmark(BookmarkCommand.Update command);
}