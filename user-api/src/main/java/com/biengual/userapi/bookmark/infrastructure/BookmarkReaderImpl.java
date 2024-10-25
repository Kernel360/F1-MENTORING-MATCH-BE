package com.biengual.userapi.bookmark.infrastructure;

import java.util.List;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkCustomRepository;
import com.biengual.userapi.bookmark.domain.BookmarkEntity;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.bookmark.domain.BookmarkReader;
import com.biengual.userapi.bookmark.domain.BookmarkRepository;
import com.biengual.userapi.bookmark.presentation.BookmarkDtoMapper;
import com.biengual.userapi.content.repository.ContentRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class BookmarkReaderImpl implements BookmarkReader {
	private final BookmarkCustomRepository bookmarkCustomRepository;
	private final ContentRepository contentRepository;
	private final BookmarkDtoMapper bookmarkDtoMapper;
	private final BookmarkRepository bookmarkRepository;

	@Override
	public List<BookmarkInfo.Position> getContentList(BookmarkCommand.GetByContents command) {
		return bookmarkCustomRepository.findBookmarksByUserIdAndScriptIndex(command.userId(), command.contentId())
			.stream()
			.map(bookmarkDtoMapper::buildPosition)
			.toList();
	}

	@Override
	public List<BookmarkInfo.MyList> getAllBookmarks(Long userId) {
		List<BookmarkEntity> bookmarks = bookmarkCustomRepository.findBookmarks(userId);
		return bookmarks.stream()
			.map(bookmark -> bookmarkDtoMapper.buildMyList(
				bookmark,
				contentRepository.findContentTypeById(bookmark.getScriptIndex()),
				contentRepository.findTitleById(bookmark.getScriptIndex())
			)).toList();
	}

	@Override
	public boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command) {
		return bookmarkCustomRepository.isBookmarkAlreadyPresent(command);
	}
}
