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
		String description,
		Double startTimeInSecond
	) {
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
