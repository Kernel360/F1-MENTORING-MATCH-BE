package com.biengual.userapi.bookmark.domain;

import java.util.List;

import com.biengual.userapi.user.domain.entity.UserEntity;

public interface BookmarkReader {
	List<BookmarkInfo.Content> getContentList(UserEntity user, Long contentId);

	List<BookmarkInfo.MyList> getAllBookmarks(Long userId);

	boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command);
}
