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

	public void createContent(ContentCommand.CrawlingContent command) {
		ContentCommand.Create contentCommand = crawlingService.getCrawlingDetail(command);
		contentService.createContent(contentCommand);
	}

	public void modifyContent(ContentCommand.Modify command) {
		contentService.updateContent(command);
	}

	public void deactivateContent(Long id) {
		contentService.deactivateContent(id);
	}
}
