package com.biengual.userapi.content.presentation;

import com.biengual.core.enums.ContentLevel;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.enums.ContentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class ContentResponseDto {
	@Builder
	public record UserScript(
		@JsonInclude(JsonInclude.Include.NON_NULL)
		Double startTimeInSecond,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		Double durationInSecond,
		String enScript,
		String koScript,
		Long bookmarkId,
		Boolean isHighlighted,
		String description
	) {
	}

	@Builder
	public record DetailRes(
		Long contentId,
		ContentType contentType,
		String category,
		String title,
		String thumbnailUrl,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String videoUrl,
		String duration,
		Integer hits,
		ContentLevel customLevel,
		ContentLevel calculatedLevel,
		Boolean isScrapped,
		BigDecimal currentLearningRate,
		BigDecimal completedLearningRate,
		List<UserScript> scriptList
	) {
	}

	public record PreviewContent(
		Long contentId,
		String title,
		String thumbnailUrl,   // coverImageUrl
		ContentType contentType,
		String duration,
		String preScripts,     // description
		String category,
		Integer hits,
		ContentLevel calculatedLevel,
		Boolean isScrapped,
		Boolean isPointRequired
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
		Integer hits,
		String duration,
		ContentLevel calculatedLevel,
		Boolean isScrapped,
		Boolean isPointRequired
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

	public record Admin(
		Long contentId,
		String title,
		String category,
		ContentType contentType,
		Integer hits,
		Integer numOfQuiz,
		ContentStatus contentStatus
	) {
	}

	@Builder
	public record AdminListRes(
		Integer pageNumber,
		Integer pageSize,
		Integer totalPages,
		Long totalElements,
		List<Admin> contents
	) {
	}
}
