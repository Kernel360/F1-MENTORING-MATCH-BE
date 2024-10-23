package com.biengual.userapi.bookmark.application;

import com.biengual.userapi.annotation.Facade;
import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.bookmark.domain.BookmarkService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class BookmarkFacade {
	private final BookmarkService bookmarkService;

	public BookmarkInfo.PositionInfo getBookmarks(BookmarkCommand.GetByContents command) {
		return bookmarkService.getBookmarks(command);
	}

	public BookmarkInfo.MyListInfo getAllBookmarks(Long userId) {
		return bookmarkService.getAllBookmarks(userId);
	}

	public void createBookmark(BookmarkCommand.Create command) {
		bookmarkService.createBookmark(command);
	}

	public BookmarkInfo.Position updateBookmark(BookmarkCommand.Update command) {
		return bookmarkService.updateBookmark(command);
	}

	public void deleteBookmark(BookmarkCommand.Delete command) {
		bookmarkService.deleteBookmark(command.userId(), command.bookmarkId());
	}
}
