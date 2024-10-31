package com.biengual.userapi.content.domain;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentType;

import lombok.Builder;

public class ContentCommand {

	public record CrawlingContent(
		String url,
		ContentType contentType
	) {
	}

	@Builder
	public record Create(
		String url,
		String title,
		String imgUrl,
		String category,
		ContentType contentType,
		List<Script> script
	) {
		public ContentDocument toDocument() {
			return ContentDocument.builder()
				.scriptList(script)
				.build();
		}

		public ContentEntity toEntity(ObjectId contentScriptId, ContentType contentType, CategoryEntity category) {
			return ContentEntity.builder()
				.contentType(contentType)
				.url(url)
				.title(title)
				.thumbnailUrl(imgUrl)
				.mongoContentId(contentScriptId.toString())
				.preScripts(script)
				.category(category)
				.build();

		}

		public CategoryEntity toCategoryEntity() {
			return CategoryEntity.builder()
				.name(this.category)
				.build();
		}
	}

	@Builder
	public record Search(
		Pageable pageable,
		String keyword,
		Long userId
	) {
	}

	@Builder
	public record GetReadingView(
		Pageable pageable,
		ContentType contentType,
		Long categoryId,
		Long userId
	) {
	}

	@Builder
	public record GetListeningView(
		Pageable pageable,
		ContentType contentType,
		Long categoryId,
		Long userId
	) {
	}

	@Builder
	public record GetReadingPreview(
		Integer size,
		String sort,
		ContentType contentType,
		Long userId
	) {
	}

	@Builder
	public record GetListeningPreview(
		Integer size,
		String sort,
		ContentType contentType,
		Long userId
	) {
	}

	@Builder
	public record GetDetail(
		Long contentId,
		Long userId
	) {
	}

	@Builder
	public record GetScrapPreview(
		Integer size,
		Long userId
	) {
	}
}
