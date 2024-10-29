package com.biengual.userapi.bookmark.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.bookmark.BookmarkEntity;
import com.biengual.userapi.bookmark.domain.*;
import com.biengual.userapi.bookmark.presentation.BookmarkDtoMapper;
import com.biengual.userapi.content.domain.ContentCustomRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@DataProvider
@RequiredArgsConstructor
public class BookmarkReaderImpl implements BookmarkReader {
	private final BookmarkCustomRepository bookmarkCustomRepository;
	private final ContentCustomRepository contentCustomRepository;
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
		List<BookmarkEntity> bookmarks = bookmarkCustomRepository.findBookmarks(userId);
		return bookmarks.stream()
			.map(bookmark -> bookmarkDtoMapper.buildMyList(
				bookmark,
				contentCustomRepository.findContentTypeById(bookmark.getScriptIndex()),
				contentCustomRepository.findTitleById(bookmark.getScriptIndex())
			)).toList();
	}

	@Override
	public boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command) {
		return bookmarkCustomRepository.isBookmarkAlreadyPresent(command);
	}
}
