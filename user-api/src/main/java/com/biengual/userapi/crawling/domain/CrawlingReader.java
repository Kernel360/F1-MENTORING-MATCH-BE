package com.biengual.userapi.crawling.domain;

import java.util.List;

import com.biengual.userapi.content.domain.ContentCommand;

public interface CrawlingReader {
    List<ContentCommand.CrawlingContent> getDailyUrlsForCrawling();
}
