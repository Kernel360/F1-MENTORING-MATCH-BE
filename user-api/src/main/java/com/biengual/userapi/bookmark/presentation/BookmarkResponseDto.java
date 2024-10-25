package com.biengual.userapi.bookmark.presentation;

import java.time.LocalDateTime;
import java.util.List;

import com.biengual.userapi.content.domain.ContentType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

public class BookmarkResponseDto {
	public record ContentList(
		Long bookmarkId,
		Long sentenceIndex,
		String description,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		Double startTimeInSecond
	) {
	}

	@Builder
	public record ContentListRes(
		List<ContentList> bookmarkList
	) {
	}

	public record MyList(
		Long bookmarkId,
		ContentType contentType,
		String bookmarkDetail,
		String description,
		Long contentId,
		String contentTitle,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
	}

	@Builder
	public record MyListRes(
		List<MyList> bookmarkMyList
	) {
	}
}
