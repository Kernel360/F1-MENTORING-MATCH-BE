package com.biengual.userapi.crawling.application;

import static com.biengual.core.constant.ServiceConstant.*;
import static com.biengual.core.response.error.code.CrawlingErrorCode.*;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import com.biengual.core.annotation.ApiClient;
import com.biengual.core.response.error.exception.CommonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@ApiClient
@RequiredArgsConstructor
public class YoutubeApiClient {
    private final RestTemplate restTemplate;

    @Value("${YOUTUBE_API_KEY}")
    private String YOUTUBE_API_KEY;

    // Youtube Data API 기반 유튜브 정보 리턴
    public ResponseEntity<String> getYoutubeInfo(String videoId) {
        // Create the request URL for the transcript service
        String url = this.getVideoUrl(videoId);

        // Set up headers with your API key
        HttpHeaders headers = this.getHttpHeaders();

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

    }

    // 스니펫 노드 추출
    public JsonNode getSnippetNode(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(body).path("items").get(0);
        } catch (JsonProcessingException e) {
            throw new CommonException(CRAWLING_JSOUP_FAILURE);
        }
    }

    // 동영상 카테고리 이름 추출
    public String getCategoryName(String categoryId) {
        String requestUrl =
            "https://www.googleapis.com/youtube/v3/videoCategories?part=snippet&regionCode=US&key=" + YOUTUBE_API_KEY;

        JsonNode rootNode = this.getJsonNodeOfUrl(requestUrl);
        JsonNode itemsNode = rootNode.path("items");

        for (JsonNode item : itemsNode) {
            if (item.path("id").asText().equals(categoryId)) {
                return item.path("snippet").path("title").asText();
            }
        }

        return UNKNOWN_CATEGORY_NAME;
    }

    // 썸네일 url 추출
    public String getThumbnailUrl(JsonNode thumbnailNode) {
        Iterator<Map.Entry<String, JsonNode>> thumbnails = thumbnailNode.fields();
        int prevWidth = 0;
        JsonNode thumbnailInfo = thumbnails.next().getValue();

        while (thumbnails.hasNext()) {
            Map.Entry<String, JsonNode> thumbnailMap = thumbnails.next();
            JsonNode comparator = thumbnailMap.getValue();
            if (Integer.parseInt(comparator.get("width").asText()) > prevWidth) {
                thumbnailInfo = comparator;
                prevWidth = Integer.parseInt(thumbnailMap.getValue().get("width").asText());
            }
        }

        return thumbnailInfo.get("url").asText();
    }

    // 유튜브 영상 길이 추출
    public Duration getYoutubeDuration(String videoId) {
        ResponseEntity<String> response = this.getYoutubeInfo(videoId);

        return Duration.parse(
            this.getSnippetNode(response.getBody()).path("contentDetails").path("duration").asText()
        );
    }

    // Internal Methods ================================================================================================
    private String getVideoUrl(String videoId) {
        return "https://www.googleapis.com/youtube/v3/videos?id=" + videoId +
            "&part=snippet, contentDetails" + "&key=" + YOUTUBE_API_KEY;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(
            "Cookie", "access_token=" +
                SecurityContextHolder.getContext().getAuthentication().getCredentials()
        );
        return headers;
    }

    private JsonNode getJsonNodeOfUrl(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        JsonNode rootNode = null;
        try {
            rootNode = new ObjectMapper().readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new CommonException(CRAWLING_JSOUP_FAILURE);
        }
        return rootNode;
    }

}