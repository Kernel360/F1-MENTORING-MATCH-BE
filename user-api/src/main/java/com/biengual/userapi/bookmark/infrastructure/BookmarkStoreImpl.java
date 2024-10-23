package com.biengual.userapi.bookmark.infrastructure;

import static com.biengual.userapi.message.error.code.BookmarkErrorCode.*;
import static com.biengual.userapi.message.error.code.ContentErrorCode.*;
import static com.biengual.userapi.message.error.code.UserErrorCode.*;

import java.util.Objects;

import org.bson.types.ObjectId;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkEntity;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.bookmark.domain.BookmarkRepository;
import com.biengual.userapi.bookmark.domain.BookmarkStore;
import com.biengual.userapi.content.domain.entity.ContentDocument;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.content.repository.ContentRepository;
import com.biengual.userapi.content.repository.ContentScriptRepository;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.script.domain.entity.YoutubeScript;
import com.biengual.userapi.user.domain.entity.UserEntity;
import com.biengual.userapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class BookmarkStoreImpl implements BookmarkStore {
	private final BookmarkRepository bookmarkRepository;
	private final UserRepository userRepository;
	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;

	@Override
	public void deleteBookmark(BookmarkCommand.Delete command) {
		bookmarkRepository.deleteBookmark(command.userId(), command.bookmarkId());
	}

	@Override
	public void saveBookmark(BookmarkCommand.Create command) {
		// TODO: user -> command.userId() 로 수정 필요함, updateUserBookmark 로직도 수정 필요
		UserEntity user = userRepository.findById(command.userId())
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		ContentDocument content = contentScriptRepository.findContentDocumentById(
			new ObjectId(contentRepository.findMongoIdByContentId(command.contentId()))
		).orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		BookmarkEntity bookmark
			= command.toEntity(extractDetail(command, content), extractStartTime(command, content));

		bookmarkRepository.save(bookmark);
		user.updateUserBookmark(bookmark);
	}

	@Override
	public BookmarkInfo.Position updateBookmark(BookmarkCommand.Update command) {
		UserEntity user = userRepository.findById(command.userId())
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		BookmarkEntity bookmark = user.getBookmarks()
			.stream()
			.filter(bookmark1 -> Objects.equals(bookmark1.getId(), command.bookmarkId()))
			.findFirst()
			.orElseThrow(() -> new CommonException(BOOKMARK_NOT_FOUND));

		bookmark.updateDescription(command.description());

		return BookmarkInfo.Position.of(bookmark);
	}

	// Internal Methods ------------------------------------------------------------------------------------------------

	private String extractDetail(BookmarkCommand.Create command, ContentDocument content) {
		return truncate(command.wordIndex() == null ?
				content.getScripts().get(Math.toIntExact(command.sentenceIndex())).getEnScript() :
				content.getScripts().get(Math.toIntExact(command.sentenceIndex())).getEnScript()
					.split(" ")[Math.toIntExact(command.wordIndex())],
			255);
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
