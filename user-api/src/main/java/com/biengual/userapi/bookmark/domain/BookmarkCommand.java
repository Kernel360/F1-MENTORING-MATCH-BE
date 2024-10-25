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
		String description
	) {
		public BookmarkEntity toEntity(String detail, Double startTime) {
			return BookmarkEntity.builder()
				.scriptIndex(this.contentId)
				.sentenceIndex(this.sentenceIndex)
				.description(this.description)
				.detail(detail)
				.startTimeInSecond(startTime)
				.userId(this.userId)
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
