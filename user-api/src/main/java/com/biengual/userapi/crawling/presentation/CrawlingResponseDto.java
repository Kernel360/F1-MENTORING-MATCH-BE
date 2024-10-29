package com.biengual.userapi.crawling.presentation;

import java.util.List;

import com.biengual.core.domain.document.content.script.Script;

public class CrawlingResponseDto {

	public record ContentDetailRes(
		String url,
		String title,
		String imgUrl,
		String category,
		List<Script> script
	) {

		public static ContentDetailRes of(
			String url, String title, String imgUrl, String category, List<Script> script
		) {
			return new ContentDetailRes(url, title, imgUrl, category, script);
		}

	}
}
