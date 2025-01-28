package com.biengual.userapi.crawling.infrastructure;

import static com.biengual.core.response.error.code.CrawlingErrorCode.*;

import java.time.Duration;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.enums.ContentType;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.crawling.application.JsoupWebCrawler;
import com.biengual.userapi.crawling.application.SeleniumWebCrawler;
import com.biengual.userapi.crawling.application.YoutubeApiClient;
import com.biengual.userapi.crawling.domain.CrawlingStore;
import com.biengual.userapi.crawling.presentation.CrawlingResponseDto;
import com.biengual.userapi.nlp.CategoryClassifier;
import com.biengual.userapi.validator.CrawlingValidator;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataProvider
@RequiredArgsConstructor
public class CrawlingStoreImpl implements CrawlingStore {
    private final CategoryClassifier categoryClassifier;
    private final CrawlingValidator crawlingValidator;
    private final YoutubeApiClient youtubeApiClient;
    private final SeleniumWebCrawler seleniumWebCrawler;
    private final JsoupWebCrawler jsoupWebCrawler;

    // TODO: 추후 클린 코드로 변경해볼 것
    @Override
    public ContentCommand.Create getYoutubeDetail(ContentCommand.CrawlingContent command) {
        // Check Already Stored In DB
        crawlingValidator.verifyCrawling(command.url());

        // Create an HTTP entity with headers
        ResponseEntity<String> response = youtubeApiClient.getYoutubeInfo(this.extractVideoId(command.url()));

        JsonNode snippetNode = youtubeApiClient.getSnippetNode(response.getBody()).path("snippet");
        JsonNode contentDetailsNode = youtubeApiClient.getSnippetNode(response.getBody()).path("contentDetails");
        String category = youtubeApiClient.getCategoryName(snippetNode.path("categoryId").asText());

        Duration duration = Duration.parse(contentDetailsNode.path("duration").asText());
        if (!crawlingValidator.verifyCrawlingYoutubeDuration(duration)) {
            throw new CommonException(CRAWLING_OUT_OF_BOUNDS);
        }

        List<Script> scripts =
            seleniumWebCrawler.getYoutubeScript(
                command.url(), Double.parseDouble(String.valueOf(duration.getSeconds()))
            );

        category = this.classifyCategory(category, scripts);

        return ContentCommand.Create.builder()
            .url(command.url())
            .title(snippetNode.path("title").asText())
            .imgUrl(youtubeApiClient.getThumbnailUrl(snippetNode.path("thumbnails")))
            .category(category)
            .contentType(ContentType.LISTENING)
            .videoDuration((int)duration.getSeconds())
            .script(scripts)
            .build();
    }

    @Override
    public ContentCommand.Create getCNNDetail(ContentCommand.CrawlingContent command) {
        CrawlingResponseDto.ContentDetailRes response = jsoupWebCrawler.fetchArticle(command.url());

        // Check Already Stored In DB
        crawlingValidator.verifyCrawling(command.url());

        String category = this.classifyCategory(response.category(), response.script());

        return ContentCommand.Create.builder()
            .url(response.url())
            .title(response.title())
            .imgUrl(response.imgUrl())
            .category(category)
            .contentType(ContentType.READING)
            .script(response.script())
            .build();
    }

    @Override
    @Transactional
    public ContentCommand.Create crawlingScheduledContent(ContentCommand.CrawlingContent command) {
        ContentCommand.Create createContentCommand = null;
        try {
            createContentCommand =
                command.contentType().equals(ContentType.READING) ?
                    this.getCNNDetail(command) :
                    this.getYoutubeDetail(command);
        } catch (Exception e) {
            log.error("{} : Crawling failed", command.url(), e);
            return null;
        }

        return createContentCommand;
    }

    // Internal Methods ------------------------------------------------------------------------------------------------
    // 동영상 id 추출
    private String extractVideoId(String youtubeUrl) {
        // Logic to extract video ID from URL
        String[] parts = youtubeUrl.split("v=");
        return parts.length > 1 ? parts[1] : "";
    }

    // 카테고리 분류
    @Deprecated
    private String classifyCategory(String category, List<Script> scripts) {
        List<String> sentences = scripts.stream()
            .map(Script::getEnScript)
            .toList();

        // return categoryClassifier.process(category, sentences);
        return null;
    }

}
