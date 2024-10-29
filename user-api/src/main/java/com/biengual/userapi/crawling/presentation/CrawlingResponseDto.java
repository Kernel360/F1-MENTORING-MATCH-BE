package com.biengual.userapi.crawling.presentation;

import com.biengual.userapi.core.entity.content.script.Script;

import java.util.List;

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
