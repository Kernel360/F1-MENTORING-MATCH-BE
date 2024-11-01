package com.biengual.userapi.nlp;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.biengual.core.constant.ServiceConstant.UNKNOWN_CATEGORY_NAME;

/**
 * category 분류기
 *
 * 사용처: 컨텐츠 생성
 *
 * @author 문찬욱
 */
@Component
@RequiredArgsConstructor
public class CategoryClassifier {
    private final NLPAnalyzer nlpAnalyzer;

    private static final List<String> CATEGORY_POS_TAGS = List.of("NN");

    // category 분류
    public String process(String categoryText, List<String> sentences) {
        if (StringUtils.isBlank(categoryText) || categoryText.equals(UNKNOWN_CATEGORY_NAME)) {
            return UNKNOWN_CATEGORY_NAME;
        }

        String[] categories = extractNounLemma(categoryText);

        if (categories.length == 0) {
            return UNKNOWN_CATEGORY_NAME;
        }

        CategoryCustomDictionary categoryCustomDictionary = new CategoryCustomDictionary();

        if (categories.length == 1) {
            return capitalizeFirstLetter(categoryCustomDictionary.replace(categories[0]));
        }

        double mostSimilarity = 0.0;
        String mostCategory = UNKNOWN_CATEGORY_NAME;

        for (String category : categories) {
            double similarity = nlpAnalyzer.sentencesWordSimilarity(sentences, category);

            if (mostSimilarity < similarity) {
                mostSimilarity = similarity;
                mostCategory = category;
            }
        }

        return capitalizeFirstLetter(categoryCustomDictionary.replace(mostCategory));

    }

    // 입력 받은 categoryText를 명사로 된 기본형 추출
    private String[] extractNounLemma(String categoryText) {
        return nlpAnalyzer.extractKeywords(categoryText, createCategoryPosTags());
    }

    // category 품사 태그 생성
    private Set<String> createCategoryPosTags() {
        return nlpAnalyzer.createPosTags(CATEGORY_POS_TAGS);
    }

    // 분류된 category 이름 반환
    private String capitalizeFirstLetter(String category) {
        if (StringUtils.isBlank(category)) {
            return UNKNOWN_CATEGORY_NAME;
        }

        CategoryCustomDictionary categoryCustomDictionary = new CategoryCustomDictionary();

        categoryCustomDictionary.replace(category);

        return category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase();
    }
}
