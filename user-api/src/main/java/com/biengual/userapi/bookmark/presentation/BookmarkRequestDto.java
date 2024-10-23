package com.biengual.userapi.bookmark.presentation;

import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.constraints.NotNull;

public class BookmarkRequestDto {

	public record ViewReq(
		@PathVariable
		Long contentId
	) {
	}

	public record CreateReq(
		@NotNull
		Long sentenceIndex,
		Long wordIndex,
		String description
	) {
	}

	public record UpdateReq(
		@PathVariable
		Long contentId,
		@NotNull
		Long bookmarkId,
		String description
	) {
	}

}
