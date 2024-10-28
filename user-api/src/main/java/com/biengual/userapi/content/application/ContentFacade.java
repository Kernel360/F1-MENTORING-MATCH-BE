package com.biengual.userapi.content.application;

import com.biengual.userapi.annotation.Facade;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentService;
import com.biengual.userapi.crawling.domain.CrawlingService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class ContentFacade {
	private final CrawlingService crawlingService;
	private final ContentService contentService;

	public void createContent(ContentCommand.CrawlingContent crawlingContentCommand) {
		ContentCommand.Create createContent = crawlingService.getCrawlingDetail(crawlingContentCommand);
		contentService.createContent(createContent);
	}

	public void modifyContent(Long id) {
		contentService.modifyContent(id);
	}
}
