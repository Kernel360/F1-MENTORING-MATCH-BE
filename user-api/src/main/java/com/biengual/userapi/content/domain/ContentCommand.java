package com.biengual.userapi.content.domain;

import com.biengual.userapi.core.entity.category.CategoryEntity;
import com.biengual.userapi.core.common.enums.ContentType;
import com.biengual.userapi.core.entity.content.ContentDocument;
import com.biengual.userapi.core.entity.content.ContentEntity;
import com.biengual.userapi.core.entity.content.script.Script;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
		String keyword
	) {
	}

	@Builder
	public record GetReadingView(
		Pageable pageable,
		ContentType contentType,
		Long categoryId
	) {
	}

	@Builder
	public record GetListeningView(
		Pageable pageable,
		ContentType contentType,
		Long categoryId
	) {
	}

	@Builder
	public record GetReadingPreview(
		Integer size,
		String sort,
		ContentType contentType
	) {
	}

	@Builder
	public record GetListeningPreview(
		Integer size,
		String sort,
		ContentType contentType
	) {
	}
}
