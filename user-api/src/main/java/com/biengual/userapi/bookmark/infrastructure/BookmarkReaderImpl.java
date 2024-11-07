package com.biengual.userapi.bookmark.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkCustomRepository;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.bookmark.domain.BookmarkReader;
import com.biengual.userapi.bookmark.presentation.BookmarkDtoMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DataProvider
@RequiredArgsConstructor
public class BookmarkReaderImpl implements BookmarkReader {
	private final BookmarkCustomRepository bookmarkCustomRepository;
	private final BookmarkDtoMapper bookmarkDtoMapper;

	@Override
	public List<BookmarkInfo.Position> getContentList(BookmarkCommand.GetByContents command) {
		return bookmarkCustomRepository.findBookmarksByUserIdAndScriptIndex(command.userId(), command.contentId())
			.stream()
			.map(bookmarkDtoMapper::buildPosition)
			.toList();
	}

	@Override
	public List<BookmarkInfo.MyList> getAllBookmarks(Long userId) {
		return bookmarkCustomRepository.findBookmarkMyListByUserId(userId);
	}

	@Override
	public boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command) {
		return bookmarkCustomRepository.isBookmarkAlreadyPresent(command);
	}
}
