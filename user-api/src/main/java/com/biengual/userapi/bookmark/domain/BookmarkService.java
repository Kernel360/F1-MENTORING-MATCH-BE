package com.biengual.userapi.bookmark.domain;

public interface BookmarkService {
	BookmarkInfo.PositionInfo getBookmarks(BookmarkCommand.GetByContents command);

	BookmarkInfo.MyListInfo getAllBookmarks(Long userId);

	BookmarkInfo.Position updateBookmark(BookmarkCommand.Update command);

	void createBookmark(BookmarkCommand.Create command);

	void deleteBookmark(Long userId, Long bookmarkId);
}
