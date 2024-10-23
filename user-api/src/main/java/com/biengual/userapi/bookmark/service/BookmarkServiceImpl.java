package com.biengual.userapi.bookmark.service;

import static com.biengual.userapi.message.error.code.BookmarkErrorCode.*;
import static com.biengual.userapi.message.error.code.ContentErrorCode.*;
import static com.biengual.userapi.message.error.code.UserErrorCode.*;

import java.util.Comparator;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.bookmark.domain.dto.BookmarkRequestDto;
import com.biengual.userapi.bookmark.domain.dto.BookmarkResponseDto;
import com.biengual.userapi.bookmark.domain.entity.BookmarkEntity;
import com.biengual.userapi.bookmark.repository.BookmarkRepository;
import com.biengual.userapi.content.domain.entity.ContentDocument;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.content.repository.ContentRepository;
import com.biengual.userapi.content.repository.ContentScriptRepository;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.script.domain.entity.YoutubeScript;
import com.biengual.userapi.user.domain.UserEntity;
import com.biengual.userapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;
	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;

	@Override
	@Transactional(readOnly = true)
	public List<BookmarkResponseDto.BookmarkListResponseDto> getBookmarks(String email, Long contentId) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		List<BookmarkEntity> bookmarks = user.getBookmarks()
			.stream()
			.filter(bookmarkEntity -> bookmarkEntity.getScriptIndex().equals(contentId))
			.sorted(Comparator.comparing(BookmarkEntity::getUpdatedAt).reversed())
			.toList();

		return bookmarks.stream()
			.map(BookmarkResponseDto.BookmarkListResponseDto::of)
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookmarkResponseDto.BookmarkMyListResponseDto> getAllBookmarks(Long userId) {
		List<BookmarkEntity> bookmarks = bookmarkRepository.getAllBookmarks(userId);

		return bookmarks.stream()
			.map(bookmark -> BookmarkResponseDto.BookmarkMyListResponseDto.of(
				bookmark,
				contentRepository.findContentTypeById(bookmark.getScriptIndex()),
				contentRepository.findTitleById(bookmark.getScriptIndex())
			)).toList();
	}

	@Override
	@Transactional
	public BookmarkResponseDto.BookmarkCreateResponse createBookmark(
		String email, BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto, Long contentId
	) {

		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));
		ContentDocument content = contentScriptRepository.findContentDocumentById(
			new ObjectId(contentRepository.findMongoIdByContentId(contentId))
		).orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		if (bookmarkRepository.isBookmarkAlreadyPresent(contentId, bookmarkRequestDto, user.getId())) {
			throw new CommonException(BOOKMARK_ALREADY_EXISTS);
		}

		BookmarkEntity bookmark = BookmarkEntity.builder()
			.scriptIndex(contentId)
			.sentenceIndex(bookmarkRequestDto.sentenceIndex())
			.wordIndex(bookmarkRequestDto.wordIndex())
			.description(bookmarkRequestDto.description())
			.detail(extractDetail(bookmarkRequestDto, content))
			.startTimeInSecond(extractStartTime(
				bookmarkRequestDto, contentRepository.findContentTypeById(contentId), content)
			)
			.build();

		bookmarkRepository.save(bookmark);
		user.updateUserBookmark(bookmark);

		return BookmarkResponseDto.BookmarkCreateResponse.of(bookmark);
	}

	@Override
	@Transactional
	public BookmarkResponseDto.BookmarkListResponseDto updateBookmark(
		BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto, Long contentId
	) {

		BookmarkEntity bookmark = bookmarkRepository.findById(bookmarkRequestDto.bookmarkId())
			.orElseThrow(() -> new CommonException(BOOKMARK_NOT_FOUND));

		bookmark.updateDescription(bookmarkRequestDto);

		return BookmarkResponseDto.BookmarkListResponseDto.of(bookmark);
	}

	@Override
	@Transactional
	public void deleteBookmark(Long userId, Long bookmarkId) {
		bookmarkRepository.deleteBookmark(userId, bookmarkId);
	}

	// Internal Methods ------------------------------------------------------------------------------------------------

	private String extractDetail(
		BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto, ContentDocument content
	) {
		return truncate(bookmarkRequestDto.wordIndex() == null ?
				content.getScripts().get(Math.toIntExact(bookmarkRequestDto.sentenceIndex())).getEnScript() :
				content.getScripts().get(Math.toIntExact(bookmarkRequestDto.sentenceIndex())).getEnScript()
					.split(" ")[Math.toIntExact(bookmarkRequestDto.wordIndex())],
			255);
	}

	private Double extractStartTime(
		BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto,
		ContentType contentTypeById, ContentDocument content
	) {
		if (contentTypeById.equals(ContentType.READING)) {
			return null;
		}

		return ((YoutubeScript)content.getScripts()
			.get(Math.toIntExact(bookmarkRequestDto.sentenceIndex())))
			.getStartTimeInSecond();
	}

	private String truncate(String content, int maxLength) {
		return content.length() > maxLength ? content.substring(0, maxLength) : content;
	}
}
