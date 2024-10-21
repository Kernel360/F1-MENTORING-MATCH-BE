package com.biengual.userapi.bookmark.domain.dto;

import java.time.LocalDateTime;

import com.biengual.userapi.bookmark.domain.entity.BookmarkEntity;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.fasterxml.jackson.annotation.JsonInclude;

public class BookmarkResponseDto {
	public record BookmarkListResponseDto(
		Long bookmarkId,
		Long sentenceIndex,
		Long wordIndex,
		String description,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		Double startTimeInSecond
	) {
		public static BookmarkListResponseDto of(BookmarkEntity bookmark) {
			return new BookmarkListResponseDto(
				bookmark.getId(),
				bookmark.getSentenceIndex(),
				bookmark.getWordIndex(),
				bookmark.getDescription(),
				bookmark.getStartTimeInSecond()
			);
		}
	}

	public record BookmarkMyListResponseDto(
		Long bookmarkId,
		ContentType contentType,
		String bookmarkDetail,
		String description,
		Long contentId,
		String contentTitle,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		public static BookmarkMyListResponseDto of(
			BookmarkEntity bookmark, ContentType contentType, String title
		) {
			return new BookmarkMyListResponseDto(
				bookmark.getId(),
				contentType,
				bookmark.getDetail(),
				bookmark.getDescription(),
				bookmark.getScriptIndex(),
				title,
				bookmark.getCreatedAt(),
				bookmark.getUpdatedAt()
			);
		}
	}

	public record BookmarkCreateResponse(
		Long bookmarkId,
		Long scriptIndex,
		Long sentenceIndex,
		Long wordIndex,
		String description
	) {
		public static BookmarkCreateResponse of(BookmarkEntity bookmark) {
			return new BookmarkCreateResponse(
				bookmark.getId(),
				bookmark.getScriptIndex(),
				bookmark.getSentenceIndex(),
				bookmark.getWordIndex(),
				bookmark.getDescription()
			);
		}
	}

}
