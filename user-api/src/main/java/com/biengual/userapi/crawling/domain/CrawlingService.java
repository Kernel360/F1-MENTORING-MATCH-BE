package com.biengual.userapi.crawling.domain;

import com.biengual.userapi.content.domain.ContentCommand;

public interface CrawlingService {
	ContentCommand.Create getCrawlingDetail(ContentCommand.GetDetail command);

	// YouTube
	ContentCommand.Create getYoutubeDetail(ContentCommand.GetDetail command);

	// CNN
	ContentCommand.Create getCNNDetail(ContentCommand.GetDetail command);
}