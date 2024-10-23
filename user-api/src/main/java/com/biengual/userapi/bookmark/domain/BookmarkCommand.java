package com.biengual.userapi.bookmark.domain;

public class BookmarkCommand {

	public record GetByContents(
		Long userId,
		Long contentId
	) {
	}

	public record Delete(
		Long userId,
		Long bookmarkId
	) {
	}

	public record Create(
		Long userId,
		Long contentId,
		Long sentenceIndex,
		Long wordIndex,
		String description
	) {
		public BookmarkEntity toEntity(String detail, Double startTime) {
			return BookmarkEntity.builder()
				.scriptIndex(contentId)
				.sentenceIndex(sentenceIndex)
				.wordIndex(wordIndex)
				.description(description)
				.detail(detail)
				.startTimeInSecond(startTime)
				.build();
		}

	}

	public record Update(
		Long userId,
		Long contentId,
		Long bookmarkId,
		String description
	) {
	}
}
