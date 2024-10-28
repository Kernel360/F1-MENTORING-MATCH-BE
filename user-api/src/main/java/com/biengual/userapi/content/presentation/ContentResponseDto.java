package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.domain.ContentEntity;
import com.biengual.userapi.content.domain.ContentType;
import com.biengual.userapi.script.domain.entity.Script;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

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
		List<PreviewContent> contentByScrapCount
	) {
	}

	// TODO: 응답에 담길 네이밍이 변경될 여지가 없다면 기존 PaginationDto처럼 통일해도 좋을 것 같음
	@Builder
	public record SearchPreviewContentsRes(
		Integer pageNumber,
		Integer pageSize,
		Integer totalPages,
		Long totalElements,
		List<PreviewContent> contents
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
		List<ViewContent> contents
	) {
	}

	@Builder
	public record ListeningViewContentsRes(
		Integer pageNumber,
		Integer pageSize,
		Integer totalPages,
		Long totalElements,
		List<ViewContent> contents
	) {
	}

	@Builder
	public record ReadingPreviewContentsRes(
		List<PreviewContent> readingPreview
	) {
	}

	@Builder
	public record ListeningPreviewContentsRes(
		List<PreviewContent> listeningPreview
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
