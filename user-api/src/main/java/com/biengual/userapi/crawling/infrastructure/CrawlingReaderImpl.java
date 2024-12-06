package com.biengual.userapi.crawling.infrastructure;

import static com.biengual.core.response.error.code.CrawlingErrorCode.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.enums.ContentType;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.crawling.domain.CrawlingReader;

@DataProvider
public class CrawlingReaderImpl implements CrawlingReader {

    @Override
    public List<ContentCommand.CrawlingContent> getDailyUrlsForCrawling() {
        List<ContentCommand.CrawlingContent> commands = new ArrayList<>();

        // 1-1. CNN 페이지 의 MORE TOP STORIES
        Document doc = null;
        try {
            doc = Jsoup.connect(URI.create("https://edition.cnn.com/").toString()).get();
        } catch (IOException e) {
            throw new CommonException(CRAWLING_JSOUP_FAILURE);
        }

        Elements elements =
            doc.select("[data-collapsed-text='More top stories'] .container_lead-plus-headlines__cards-wrapper");
        for (Element element : elements) {
            commands.add(
                ContentCommand.CrawlingContent.builder()
                    .url(Objects.requireNonNull(element.select("a").first()).attr("href"))
                    .contentType(ContentType.READING)
                    .build()
            );
        }

        // 1-2. Youtube 채널의 rss

        return commands;
    }
}
