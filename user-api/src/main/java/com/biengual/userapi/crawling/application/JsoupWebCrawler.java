package com.biengual.userapi.crawling.application;

import static com.biengual.core.response.error.code.CrawlingErrorCode.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.biengual.core.annotation.WebCrawler;
import com.biengual.core.domain.document.content.script.CNNScript;
import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.crawling.presentation.CrawlingResponseDto;

import lombok.RequiredArgsConstructor;

@WebCrawler
@RequiredArgsConstructor
public class JsoupWebCrawler {
    private final TranslateApiClient translateApiClient;

    // READING - CNN
    // Jsoup 크롤링 - 정적 크롤링
    public CrawlingResponseDto.ContentDetailRes fetchArticle(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new CommonException(CRAWLING_JSOUP_FAILURE);
        }

        // 제목 추출
        String title = doc.select("h1.headline__text").text();

        // 카테고리 추출
        Element categoryElement = doc.selectFirst("meta[name=meta-section]");
        String category = categoryElement != null ? categoryElement.attr("content") : "Unknown Category";

        // 이미지 URL 추출
        Elements images = doc.select("img.image__dam-img[src]");
        if (images.isEmpty()) {
            throw new CommonException(CRAWLING_JSOUP_FAILURE);
        }

        String imgUrl = "%s.jpg".formatted(
            images.get(0).attr("src")
                .split(".jpg")[0]
        );
        int preWidth = this.getWidthOfImage(images.get(0));

        for (Element image : images) {
            if (preWidth < this.getWidthOfImage(image)) {
                imgUrl = "%s.jpg".formatted(
                    image.attr("src")
                        .split(".jpg")[0]
                );
                preWidth = this.getWidthOfImage(image);
            }
        }

        // 본문 추출
        Elements paragraphs = doc.select("div.article__content p");
        StringBuilder fullText = new StringBuilder();
        for (Element paragraph : paragraphs) {
            fullText.append(paragraph.text()).append(" ");
        }

        // 본문을 문장 단위로 나누기
        List<String> sentences = this.splitIntoSentences(fullText.toString());
        return CrawlingResponseDto.ContentDetailRes.of(
            url,
            title,
            imgUrl,
            category,
            sentences.stream()
                .map(sentence -> (Script)CNNScript.of(
                        sentence, translateApiClient.translate(sentence, "en", "ko")
                    )
                ).toList()
        );
    }

    // Internal Methods ================================================================================================
    private int getWidthOfImage(Element imginfo) {
        return Integer.parseInt(
            imginfo.attr("src")
                .split("w_")[1]
                .split(",c")[0]
        );
    }

    private List<String> splitIntoSentences(String text) {
        List<String> sentences = new ArrayList<>();

        // 정규 표현식을 사용하여 문장 단위로 나누기
        String[] splitSentences = text.split("(?<=[.!?])\\s+");

        for (String sentence : splitSentences) {
            sentences.add(sentence.trim());
        }

        return sentences;
    }
}
