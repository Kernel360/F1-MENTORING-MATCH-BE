package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.domain.ContentDocument;
import com.biengual.userapi.content.domain.ContentEntity;
import com.biengual.userapi.content.domain.ContentType;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.script.domain.entity.Script;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

import static com.biengual.userapi.message.error.code.CategoryErrorCode.CATEGORY_NOT_FOUND;

public class ContentResponseDto {

	@Builder
	public record DetailRes(
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
		public static DetailRes of(ContentEntity content, ContentDocument contentDocument) {
			return DetailRes.builder()
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

	public record PreviewRes(
		Long contentId,
		String title,
		String thumbnailUrl,	// coverImageUrl
		ContentType contentType,
		String preScripts,	// description
		String category,
		int hits
	) {
		public static PreviewRes of(ContentEntity content) {
			return new PreviewRes(
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

	@Builder
	public record SearchPreviewContentsRes(
		Integer pageNumber,
		Integer pageSize,
		Integer totalPages,
		Long totalElements,
		List<PreviewContent> searchPreview
	) {
	}

	public record ViewContent(
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
	public record ReadingViewContentsRes(
		Integer pageNumber,
		Integer pageSize,
		Integer totalPages,
		Long totalElements,
		List<ViewContent> readingView
	) {
	}

	@Builder
	public record ListeningViewContentsRes(
		Integer pageNumber,
		Integer pageSize,
		Integer totalPages,
		Long totalElements,
		List<ViewContent> listeningView
	) {
	}

	public record GetByScrapCount(
		Long contentId,
		String title,
		String thumbnailUrl,
		ContentType contentType,
		String preScripts,
		String category,
		Long countScrap
	) {
		public static GetByScrapCount of(ContentEntity content, Long count){
			return new GetByScrapCount(
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
