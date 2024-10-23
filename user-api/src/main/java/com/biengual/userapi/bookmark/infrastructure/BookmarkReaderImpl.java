package com.biengual.userapi.bookmark.infrastructure;

import java.util.Comparator;
import java.util.List;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkEntity;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.bookmark.domain.BookmarkReader;
import com.biengual.userapi.bookmark.domain.BookmarkRepository;
import com.biengual.userapi.content.repository.ContentRepository;
import com.biengual.userapi.user.domain.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class BookmarkReaderImpl implements BookmarkReader {
	private final BookmarkRepository bookmarkRepository;
	private final ContentRepository contentRepository;

	@Override
	public List<BookmarkInfo.Position> getContentList(UserEntity user, Long contentId) {
		return user.getBookmarks()
			.stream()
			.filter(bookmarkEntity -> bookmarkEntity.getScriptIndex().equals(contentId))
			.sorted(Comparator.comparing(BookmarkEntity::getUpdatedAt).reversed())
			.map(BookmarkInfo.Position::of)
			.toList();
	}

	@Override
	public List<BookmarkInfo.MyList> getAllBookmarks(Long userId) {
		List<BookmarkEntity> bookmarks = bookmarkRepository.getAllBookmarks(userId);
		return bookmarks.stream()
			.map(bookmark -> BookmarkInfo.MyList.of(
				bookmark,
				contentRepository.findContentTypeById(bookmark.getScriptIndex()),
				contentRepository.findTitleById(bookmark.getScriptIndex())
			)).toList();
	}

	@Override
	public boolean isBookmarkAlreadyPresent(BookmarkCommand.Create command) {
		return bookmarkRepository.isBookmarkAlreadyPresent(command);
	}
}
