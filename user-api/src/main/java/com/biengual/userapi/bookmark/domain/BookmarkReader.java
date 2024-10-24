package com.biengual.userapi.bookmark.domain;

import com.biengual.userapi.user.domain.UserEntity;

import java.util.List;

public interface BookmarkReader {
	List<BookmarkInfo.Position> getContentList(UserEntity user, Long contentId);

	List<BookmarkInfo.MyList> getAllBookmarks(Long userId);

	boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command);
}
