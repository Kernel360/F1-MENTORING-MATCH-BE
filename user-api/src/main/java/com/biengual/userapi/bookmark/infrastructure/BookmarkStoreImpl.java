package com.biengual.userapi.bookmark.infrastructure;

import static com.biengual.core.response.error.code.BookmarkErrorCode.*;
import static com.biengual.core.response.error.code.ContentErrorCode.*;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.content.script.YoutubeScript;
import com.biengual.core.domain.entity.bookmark.BookmarkEntity;
import com.biengual.core.enums.ContentType;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.bookmark.domain.*;
import com.biengual.userapi.bookmark.presentation.BookmarkDtoMapper;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;


@DataProvider
@RequiredArgsConstructor
public class BookmarkStoreImpl implements BookmarkStore {
	private final BookmarkRepository bookmarkRepository;
	private final BookmarkCustomRepository bookmarkCustomRepository;
	private final ContentDocumentRepository contentDocumentRepository;
	private final ContentCustomRepository contentCustomRepository;
	private final BookmarkDtoMapper bookmarkDtoMapper;

	@Override
	public void deleteBookmark(BookmarkCommand.Delete command) {
		long execute = bookmarkCustomRepository.deleteBookmark(command);

		if (execute < 1) {
			throw new CommonException(BOOKMARK_NOT_FOUND);
		}
	}

	@Override
	public void saveBookmark(BookmarkCommand.Create command) {
		ContentDocument content = contentDocumentRepository.findContentDocumentById(
			new ObjectId(contentCustomRepository.findMongoIdByContentId(command.contentId()))
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
		if (contentCustomRepository.findContentTypeById(command.contentId()).equals(ContentType.READING)) {
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
