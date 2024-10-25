package com.biengual.userapi.bookmark.infrastructure;

import static com.biengual.userapi.message.error.code.BookmarkErrorCode.*;
import static com.biengual.userapi.message.error.code.ContentErrorCode.*;

import org.bson.types.ObjectId;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkCustomRepository;
import com.biengual.userapi.bookmark.domain.BookmarkEntity;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.bookmark.domain.BookmarkRepository;
import com.biengual.userapi.bookmark.domain.BookmarkStore;
import com.biengual.userapi.bookmark.presentation.BookmarkDtoMapper;
import com.biengual.userapi.content.domain.entity.ContentDocument;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.content.repository.ContentRepository;
import com.biengual.userapi.content.repository.ContentScriptRepository;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.script.domain.entity.YoutubeScript;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class BookmarkStoreImpl implements BookmarkStore {
	private final BookmarkRepository bookmarkRepository;
	private final BookmarkCustomRepository bookmarkCustomRepository;
	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;
	private final BookmarkDtoMapper bookmarkDtoMapper;

	@Override
	public void deleteBookmark(BookmarkCommand.Delete command) {
		if (!bookmarkCustomRepository.deleteBookmark(command)) {
			throw new CommonException(BOOKMARK_NOT_FOUND);
		}
	}

	@Override
	public void saveBookmark(BookmarkCommand.Create command) {
		ContentDocument content = contentScriptRepository.findContentDocumentById(
			new ObjectId(contentRepository.findMongoIdByContentId(command.contentId()))
		).orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		BookmarkEntity bookmark
			= command.toEntity(extractDetail(command, content), extractStartTime(command, content));

		bookmarkRepository.save(bookmark);
	}

	@Override
	public BookmarkInfo.Position updateBookmark(BookmarkCommand.Update command) {

		BookmarkEntity bookmark = bookmarkRepository.findByIdAndUserId(command.bookmarkId(), command.userId())
			.orElseThrow(() -> new CommonException(BOOKMARK_NOT_FOUND));

		bookmark.updateDescription(command.description());

		return bookmarkDtoMapper.buildPosition(bookmark);
	}

	// Internal Methods ------------------------------------------------------------------------------------------------

	private String extractDetail(BookmarkCommand.Create command, ContentDocument content) {
		return truncate(
			content.getScripts().get(Math.toIntExact(command.sentenceIndex())).getEnScript(), 255
		);
	}

	private Double extractStartTime(BookmarkCommand.Create command, ContentDocument content) {
		if (contentRepository.findContentTypeById(command.contentId()).equals(ContentType.READING)) {
			return null;
		}

		return ((YoutubeScript)content.getScripts()
			.get(Math.toIntExact(command.sentenceIndex())))
			.getStartTimeInSecond();
	}

	private String truncate(String content, int maxLength) {
		return content.length() > maxLength ? content.substring(0, maxLength) : content;
	}
}
