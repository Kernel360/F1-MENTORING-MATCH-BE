package com.biengual.userapi.bookmark.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.biengual.userapi.content.domain.enums.ContentType;

import lombok.Builder;

public class BookmarkInfo {

	@Builder
	public record Content(
		Long bookmarkId,
		Long sentenceIndex,
		Long wordIndex,
		String description,
		Double startTimeInSecond
	) {

		public static Content of(BookmarkEntity bookmark) {
			return Content.builder()
				.bookmarkId(bookmark.getId())
				.sentenceIndex(bookmark.getSentenceIndex())
				.wordIndex(bookmark.getWordIndex())
				.description(bookmark.getDescription())
				.startTimeInSecond(bookmark.getStartTimeInSecond())
				.build();
		}
	}

	@Builder
	public record ContentInfo(
		List<Content> bookmarkList
	) {
		public static ContentInfo of(List<Content> contentInfo){
			return ContentInfo.builder()
				.bookmarkList(contentInfo)
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
			return  MyListInfo.builder()
				.bookmarkMyList(myListInfo)
				.build();
		}
	}

	public record Create(
		Long bookmarkId,
		Long scriptIndex,
		Long sentenceIndex,
		Long wordIndex,
		String description
	) {
		public static BookmarkInfo.Create of(BookmarkEntity bookmark) {
			return new BookmarkInfo.Create(
				bookmark.getId(),
				bookmark.getScriptIndex(),
				bookmark.getSentenceIndex(),
				bookmark.getWordIndex(),
				bookmark.getDescription()
			);
		}
	}

}
