package com.biengual.userapi.bookmark.domain.dto;

import jakarta.validation.constraints.NotNull;

public class BookmarkRequestDto {
	public record BookmarkCreateRequest(
		@NotNull Long sentenceIndex,
		Long wordIndex,
		String description
	) {

	}

	public record BookmarkUpdateRequest(
		@NotNull Long bookmarkId,
		String description
	) {
	}

}
