package com.biengual.userapi.crawling.domain;

import com.biengual.userapi.content.domain.ContentCommand;

public interface CrawlingService {
	ContentCommand.Create getCrawlingDetail(ContentCommand.CrawlingContent command);

	// YouTube
	ContentCommand.Create getYoutubeDetail(ContentCommand.CrawlingContent command);

	// CNN
	ContentCommand.Create getCNNDetail(ContentCommand.CrawlingContent command);
}