package com.biengual.userapi.crawling.application;

import static com.biengual.core.constant.ServiceConstant.*;

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
        String url = this.getUrl(videoId);

        // Set up headers with your API key
        HttpHeaders headers = this.getHttpHeaders();

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

    }

    // 스니펫 노드 추출
    public JsonNode getSnippetNode(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(body).path("items").get(0);
    }

    // 동영상 카테고리 이름 추출
    public String getCategoryName(String categoryId) throws Exception {
        String requestUrl =
            "https://www.googleapis.com/youtube/v3/videoCategories?part=snippet&regionCode=US&key=" + YOUTUBE_API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());
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

    // Internal Methods ================================================================================================
    private String getUrl(String videoId) {
        return "https://www.googleapis.com/youtube/v3/videos?id=" + videoId +
            "&part=snippet, contentDetails" + "&key=" + YOUTUBE_API_KEY;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "access_token=" +
            SecurityContextHolder.getContext().getAuthentication().getCredentials());
        return headers;
    }
}