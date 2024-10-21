package com.biengual.userapi.bookmark.repository.custom;

import com.biengual.userapi.bookmark.domain.dto.BookmarkRequestDto;
import com.biengual.userapi.bookmark.domain.entity.BookmarkEntity;

import java.util.List;

public interface BookmarkRepositoryCustom {
	Long deleteBookmark(Long userId, Long bookmarkId);
	List<BookmarkEntity> getAllBookmarks(Long userId);
	boolean isBookmarkAlreadyPresent(Long scriptIndex, BookmarkRequestDto.BookmarkCreateRequest request, Long userId);
}
