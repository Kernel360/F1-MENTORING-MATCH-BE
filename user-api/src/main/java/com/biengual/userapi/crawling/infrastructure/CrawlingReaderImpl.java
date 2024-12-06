package com.biengual.userapi.crawling.infrastructure;

import static com.biengual.core.constant.CrawlingAutomationConstant.*;
import static com.biengual.core.response.error.code.CrawlingErrorCode.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.enums.ContentType;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.crawling.application.YoutubeApiClient;
import com.biengual.userapi.crawling.domain.CrawlingReader;
import com.biengual.userapi.validator.CrawlingValidator;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class CrawlingReaderImpl implements CrawlingReader {
    private final CrawlingValidator crawlingValidator;
    private final YoutubeApiClient youtubeApiClient;

    @Override
    public List<ContentCommand.CrawlingContent> getDailyUrlsForCrawling() {
        List<ContentCommand.CrawlingContent> commands = new ArrayList<>();

        // 1-1. CNN 페이지 의 MORE TOP STORIES
        Document doc = null;
        try {
            doc = Jsoup.connect(URI.create(CNNBASEURL).toString()).get();
        } catch (IOException e) {
            throw new CommonException(CRAWLING_JSOUP_FAILURE);
        }

        Elements elements =
            doc.select("[data-collapsed-text='More top stories'] .container_lead-plus-headlines__cards-wrapper");

        Elements links = elements.get(0).select("a");
        for (int i = 1; i < links.size(); ++i) {
            String href = links.get(i).attr("href");
            String url = CNNBASEURL + href;
            if (!href.isEmpty() && !crawlingValidator.verifyCrawlingUrlAlreadyExists(url)) {
                commands.add(ContentCommand.CrawlingContent.of(url, ContentType.READING));
            }
        }

        // 1-2. Youtube 채널 동영상 링크
        // 채널 별로 매일 1개 씩 크롤링
        List<String> tempUrls = new ArrayList<>();
        tempUrls.add(this.getVideoUrls(IDOFTEDED));
        tempUrls.add(this.getVideoUrls(IDOFVOX));
        tempUrls.add(this.getVideoUrls(IDOFLIVENOGGIN));
        tempUrls.add(this.getVideoUrls(IDOFSCISHOW));

        commands.addAll(
            tempUrls.stream()
                .map(url -> ContentCommand.CrawlingContent.of(url, ContentType.LISTENING))
                .toList()
        );

        return commands;
    }

    public String getVideoUrls(String channelId) {
        URL feedUrl = null;
        SyndFeed feed = null;
        SyndFeedInput input = new SyndFeedInput();

        try {
            feedUrl = new URL(RSSOFYOUTUBE + channelId);
            feed = input.build(new XmlReader(feedUrl));
        } catch (FeedException | IOException e) {
            throw new CommonException(CRAWLING_RSS_PARSING_ERROR);
        }

        return feed.getEntries().stream()
            .map(SyndEntry::getLink)
            .filter(link -> {
                Duration duration = youtubeApiClient.getYoutubeDuration(this.extractVideoId(link));
                return crawlingValidator.verifyCrawlingYoutubeDuration(duration);
            })
            .filter(Predicate.not(crawlingValidator::verifyCrawlingUrlAlreadyExists))
            .findFirst()
            .orElseThrow(() -> new CommonException(CRAWLING_NO_NEW_CONTENT));
    }

    // Internal Methods ================================================================================================
    // 동영상 id 추출
    private String extractVideoId(String youtubeUrl) {
        // Logic to extract video ID from URL
        String[] parts = youtubeUrl.split("v=");
        return parts.length > 1 ? parts[1] : "";
    }

}
