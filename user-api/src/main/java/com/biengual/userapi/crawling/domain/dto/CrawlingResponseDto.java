package com.biengual.userapi.crawling.domain.dto;

import com.biengual.userapi.category.domain.CategoryEntity;
import com.biengual.userapi.content.domain.ContentDocument;
import com.biengual.userapi.content.domain.ContentEntity;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.script.domain.entity.Script;
import org.bson.types.ObjectId;

import java.util.List;

public class CrawlingResponseDto {

	public record CrawlingContentResponseDto(
		String url,
		String title,
		String imgUrl,
		String category,
		List<Script> script

	) {
		public ContentDocument toDocument(){
			return ContentDocument.builder()
				.scriptList(script)
				.build();
		}
		public ContentEntity toEntity(ObjectId contentScriptId, ContentType contentType, CategoryEntity category){
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
		public static CrawlingContentResponseDto of(
			String url, String title, String imgUrl, String category, List<Script> script
		) {
			return new CrawlingContentResponseDto(url, title, imgUrl, category, script);
		}
		public CategoryEntity toCategoryEntity() {
			return CategoryEntity.builder()
				.name(this.category)
				.build();
		}
	}
}
