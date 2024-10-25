package com.biengual.userapi.content.domain.dto;

import static com.biengual.userapi.message.error.code.CategoryErrorCode.*;

import java.util.List;

import com.biengual.userapi.content.domain.entity.ContentDocument;
import com.biengual.userapi.content.domain.entity.ContentEntity;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.script.domain.entity.Script;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

public class ContentResponseDto {

	public record ContentCreateResponseDto(
		String scriptId,
		Long contentId
	) {

	}

	public record ContentUpdateResponseDto(
		Long contentId
	) {

	}

	@Builder
	public record ContentDetailResponseDto(
		Long contentId,
		ContentType contentType,
		String category,
		String title,
		String thumbnailUrl,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String videoUrl,
		Integer hits,
		List<Script> scriptList
	) {
		public static ContentDetailResponseDto of(ContentEntity content, ContentDocument contentDocument) {
			return ContentDetailResponseDto.builder()
				.contentId(content.getId())
				.contentType(content.getContentType())
				.category(content.getCategory().getName())
				.title(content.getTitle())
				.thumbnailUrl(content.getThumbnailUrl())
				.videoUrl(toListeningUrl(content))
				.hits(content.getHits())
				.scriptList(contentDocument.getScripts())
				.build();
		}

		private static String toListeningUrl(ContentEntity content) {
			switch (content.getContentType()) {
				case LISTENING -> {
					return content.getUrl();
				}
				case READING -> {
					return null;
				}
				default -> throw new CommonException(CATEGORY_NOT_FOUND);
			}
		}
	}

	public record ContentPreviewResponseDto(
		Long contentId,
		String title,
		String thumbnailUrl,
		ContentType contentType,
		String preScripts,
		String category,
		int hits
	) {
		public static ContentPreviewResponseDto of(ContentEntity content) {
			return new ContentPreviewResponseDto(
				content.getId(),
				content.getTitle(),
				content.getThumbnailUrl(),
				content.getContentType(),
				content.getPreScripts(),
				content.getCategory().getName(),
				content.getHits()
			);
		}
	}

	public record PreviewContent(
		Long contentId,
		String title,
		String thumbnailUrl,
		ContentType contentType,
		String preScripts,
		String category,
		Integer hits
	) {
	}

	@Builder
	public record ScrapPreviewContentsRes(
		List<PreviewContent> scrapPreview
	) {
	}

	public record ContentByScrapCountDto(
		Long contentId,
		String title,
		String thumbnailUrl,
		ContentType contentType,
		String preScripts,
		String category,
		Long countScrap
	) {
		public static ContentByScrapCountDto of(ContentEntity content, Long count){
			return new ContentByScrapCountDto(
				content.getId(),
				content.getTitle(),
				content.getThumbnailUrl(),
				content.getContentType(),
				content.getPreScripts(),
				content.getCategory().getName(),
				count
			);
		}
	}

}
