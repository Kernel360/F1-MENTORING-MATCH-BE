package com.biengual.userapi.content.domain;

import com.biengual.userapi.category.domain.CategoryEntity;
import com.biengual.userapi.script.domain.entity.Script;
import lombok.Builder;
import org.bson.types.ObjectId;

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
	public record Modify(
		Long id,
		String url,
		String title,
		List<Script> script,
		ContentStatus contentStatus
	) {
	}

}
