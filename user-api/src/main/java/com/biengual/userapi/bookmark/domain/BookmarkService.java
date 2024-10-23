package com.biengual.userapi.bookmark.domain;

public interface BookmarkService {
	BookmarkInfo.ContentInfo getBookmarks(BookmarkCommand.GetByContents command);

	BookmarkInfo.MyListInfo getAllBookmarks(Long userId);

	BookmarkInfo.Content updateBookmark(BookmarkCommand.Update command);

	BookmarkInfo.Create createBookmark(BookmarkCommand.Create command);

	void deleteBookmark(Long userId, Long bookmarkId);
}
