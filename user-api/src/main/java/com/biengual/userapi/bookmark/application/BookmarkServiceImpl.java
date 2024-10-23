package com.biengual.userapi.bookmark.application;

import static com.biengual.userapi.message.error.code.BookmarkErrorCode.*;
import static com.biengual.userapi.message.error.code.ContentErrorCode.*;
import static com.biengual.userapi.message.error.code.UserErrorCode.*;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.bookmark.domain.BookmarkReader;
import com.biengual.userapi.bookmark.domain.BookmarkService;
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

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
	private final UserRepository userRepository;
	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;

	private final BookmarkReader bookmarkReader;
	private final BookmarkStore bookmarkStore;

	@Override
	@Transactional(readOnly = true)
	public BookmarkInfo.PositionInfo getBookmarks(BookmarkCommand.GetByContents command) {
		UserEntity user = userRepository.findById(command.userId())
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));
		return BookmarkInfo.PositionInfo.of(bookmarkReader.getContentList(user, command.contentId()));
	}

	@Override
	@Transactional(readOnly = true)
	public BookmarkInfo.MyListInfo getAllBookmarks(Long userId) {
		return BookmarkInfo.MyListInfo.of(bookmarkReader.getAllBookmarks(userId));
	}

	@Override
	@Transactional
	public void createBookmark(BookmarkCommand.Create command) {
		// TODO: content 쪽 reader 생성 되면 수정
		ContentDocument content = contentScriptRepository.findContentDocumentById(
			new ObjectId(contentRepository.findMongoIdByContentId(command.contentId()))
		).orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		if (bookmarkReader.isBookmarkAlreadyPresent(command)) {
			throw new CommonException(BOOKMARK_ALREADY_EXISTS);
		}

		bookmarkStore.saveBookmark(command, extractDetail(command, content), extractStartTime(command, content));
	}

	@Override
	@Transactional
	public BookmarkInfo.Position updateBookmark(BookmarkCommand.Update command) {
		return bookmarkStore.updateBookmark(command);
	}

	@Override
	@Transactional
	public void deleteBookmark(Long userId, Long bookmarkId) {
		bookmarkStore.deleteBookmark(userId, bookmarkId);
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
