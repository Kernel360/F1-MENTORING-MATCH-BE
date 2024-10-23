package com.biengual.userapi.bookmark.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.biengual.userapi.content.domain.enums.ContentType;

import lombok.Builder;

public class BookmarkInfo {

	@Builder
	public record Position(
		Long bookmarkId,
		Long sentenceIndex,
		Long wordIndex,
		String description,
		Double startTimeInSecond
	) {

		public static Position of(BookmarkEntity bookmark) {
			return Position.builder()
				.bookmarkId(bookmark.getId())
				.sentenceIndex(bookmark.getSentenceIndex())
				.wordIndex(bookmark.getWordIndex())
				.description(bookmark.getDescription())
				.startTimeInSecond(bookmark.getStartTimeInSecond())
				.build();
		}
	}

	@Builder
	public record PositionInfo(
		List<Position> bookmarkList
	) {
		public static PositionInfo of(List<Position> positionInfo) {
			return PositionInfo.builder()
				.bookmarkList(positionInfo)
				.build();
		}
	}

	@Builder
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
		public static BookmarkInfo.MyList of(
			BookmarkEntity bookmark, ContentType contentType, String title
		) {
			return MyList.builder()
				.bookmarkId(bookmark.getId())
				.contentType(contentType)
				.bookmarkDetail(bookmark.getDetail())
				.description(bookmark.getDescription())
				.contentId(bookmark.getScriptIndex())
				.contentTitle(title)
				.createdAt(bookmark.getCreatedAt())
				.updatedAt(bookmark.getUpdatedAt())
				.build();
		}
	}

	@Builder
	public record MyListInfo(
		List<BookmarkInfo.MyList> bookmarkMyList
	) {
		public static MyListInfo of(List<MyList> myListInfo) {
			return MyListInfo.builder()
				.bookmarkMyList(myListInfo)
				.build();
		}
	}

}
