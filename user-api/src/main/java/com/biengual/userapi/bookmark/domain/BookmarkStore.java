package com.biengual.userapi.bookmark.domain;

public interface BookmarkStore {
	void deleteBookmark(BookmarkCommand.Delete command);

	void saveBookmark(BookmarkCommand.Create command);

	BookmarkInfo.Position updateBookmark(BookmarkCommand.Update command);
}