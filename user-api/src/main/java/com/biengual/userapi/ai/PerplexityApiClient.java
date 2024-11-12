package com.biengual.userapi.ai;

import static com.biengual.core.response.error.code.QuestionErrorCode.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;

import com.biengual.core.annotation.ApiClient;
import com.biengual.core.response.error.exception.CommonException;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

@ApiClient
public class PerplexityApiClient {

    @Value("${perplexity.api-key}")
    private String apiKey;

    @Value("${perplexity.base-url}")
    private String baseUrl;

    public JSONObject getQuestionsByPerplexity(String prompt) {
        CompletableFuture<HttpResponse<String>> futureResponse;
        HttpResponse<String> response;
        try {
            // 요청 응답
            futureResponse = getStringHttpResponse(prompt);
            response = futureResponse.get();
        } catch (UnirestException | ExecutionException | InterruptedException e) {
            throw new CommonException(QUESTION_GENERATE_API_ERROR);
        }

        // 응답 파싱
        JSONObject responseJson = new JSONObject(response.getBody());

        // 응답에서 content 추출
        String content = responseJson.getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content");

        // 결과 JSONObject 생성
        JSONObject result = new JSONObject();

        // 각 섹션을 개별적으로 파싱
        result.put("MATCH", parseSection(content, "MATCH"));
        result.put("WORD", parseSection(content, "WORD"));
        result.put("BLANK", parseSection(content, "BLANK"));
        result.put("ORDER", parseSection(content, "ORDER"));

        return result;
    }

    // Internal Methods ===============================================================================================
    // Perplexity API 요청을 비동기로 처리하는 메소드
    @NotNull
    private CompletableFuture<HttpResponse<String>> getStringHttpResponse(String prompt) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "llama-3.1-sonar-large-128k-online");

        JSONArray messages = new JSONArray();
        String quizOptions = """
             다음 형식의 JSON만을 응답으로 제공해주세요. 다른 설명이나 텍스트는 포함하지 마세요: 
            {
              "MATCH": [{"question": "...", "examples": ["...", "...", "...", "..."], "answer": "...", "hint": "..."}],
              "WORD": [{"question": "...", "examples": ["...", "...", "...", "..."], "answer": "...", "hint": "..."}],
              "BLANK": [{"question": "...", "examples": ["...", "...", "...", "..."], "answer": "...", "hint": "..."}],
              "ORDER": [{"question": "...", "examples": ["...", "...", "...", "..."], "answer": ["...", "...", "...", "..."], "hint": "..."}]
            }
            제시한 글에 대해 학습에 도움이 되도록 각 유형별로 영어로 보기가 있는 객관식 문제를 3개씩, 모든 문제에 대한 보기는 4개씩 생성해주세요.
            각 문제에 대한 힌트도 추가해주세요.
            정답은 examples 기준 인덱스로 주세요.
            문제에 대한 각 유형은 다음과 같습니다.
            MATCH: 내용 일치, WORD: 영단어 뜻 맞추기, BLANK: 문장의 빈칸에 들어갈 단어 맞추기, 
            ORDER: 다음 지침을 따라 문제를 생성하세요:
                  1. 본문에서 중요한 완전한 영어 문장을 선택하세요.
                  2. 이 문장을 4개의 의미 있는 부분으로 나누세요.
                  3. 나눈 부분들을 무작위로 섞어 examples에 넣으세요.
                  4. question에는 "Arrange the following parts to form a correct sentence:"를 넣으세요.
                  5. answer에는 올바른 순서의 인덱스를 배열로 넣으세요. 예: [2, 0, 3, 1]
                  6. 각 부분에서 불필요한 마침표나 대문자는 제거하세요.
                  7. hint에는 문장의 의미나 구조에 대한 힌트를 제공하세요.

           모든 유형의 문제에 대해 명확하고 정확한 문제와 보기를 생성하세요.
            """;

        messages.put(new JSONObject().put("role", "user").put("content", prompt + quizOptions));
        requestBody.put("messages", messages);

        requestBody.put("max_tokens", 2000);
        requestBody.put("temperature", 0.7);

        HttpResponse<String> response = Unirest.post(baseUrl + "/chat/completions")
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .asString();

        return CompletableFuture.completedFuture(response);
    }

    // 응답에서 QuestionType에 해당하는 Question만 추출하는 메소드
    private JSONArray parseSection(String content, String sectionName) {
        int startIndex = content.indexOf("\"" + sectionName + "\": [");
        if (startIndex == -1) {
            return new JSONArray();
        }

        startIndex = content.indexOf("[", startIndex);
        int endIndex = findMatchingBracket(content, startIndex);

        if (endIndex == -1) {
            return new JSONArray();
        }

        String sectionContent = content.substring(startIndex, endIndex + 1);
        return new JSONArray(sectionContent);
    }

    // JSON 타입에서 괄호가 빠지는 이슈를 해결하기 위한 메소드
    private int findMatchingBracket(String content, int startIndex) {
        int count = 1;
        for (int i = startIndex + 1; i < content.length(); i++) {
            if (content.charAt(i) == '[') {
                count++;
            } else if (content.charAt(i) == ']') {
                count--;
            }
            if (count == 0) {
                return i;
            }
        }
        return -1;
    }
}

