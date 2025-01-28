package com.biengual.userapi.nlp;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * NLP 분석기
 * 단어 또는 문장 분석, 각 단어 간 유사도 분석 가능
 *
 * 유사도 알고리즘은 코사인 유사도 사용
 *
 * @author 문찬욱
 */
@Deprecated
@Component
@RequiredArgsConstructor
public class NLPAnalyzer {
    /*private final StanfordCoreNLP pipeline;
    private final Word2Vec word2Vec;

    private static final List<String> SENTENCE_POS_TAGS = List.of("NN", "VB");

    // 문장들과 단어 간 유사도 계산
    public double sentencesWordSimilarity(List<String> sentences, String word) {
        double similarity = 0.0;

        for (String sentence : sentences) {
            similarity += sentenceWordSimilarity(sentence, word);
        }

        // 평균 계산
        return similarity / sentences.size();
    }



    // 문장과 단어 간 유사도 계산
    public double sentenceWordSimilarity(String sentence, String word) {

        // 단어 벡터 가져오기
        double[] wordVector = word2Vec.getWordVector(word);
        if (wordVector == null) {
            return 0.0;
        }

        // 문장에서 keyword 추출
        String[] keywords = extractKeywords(sentence, createPosTags(SENTENCE_POS_TAGS));

        if (keywords.length == 0) {
            return 0.0;
        }

        // 코사인 유사도 계산을 위해 문장 벡터를 단어 벡터와 차원을 동일하게 만듦
        double[] sentenceVector = new double[wordVector.length];
        Arrays.fill(sentenceVector, 0.0);

        // 문장 벡터 계산 (단어 벡터의 평균)
        for (String keyword : keywords) {
            double[] keywordVector = getWordVector(keyword);
            if (keywordVector != null) {
                for (int i = 0; i < sentenceVector.length; i++) {
                    sentenceVector[i] += keywordVector[i];
                }
            }
        }

        // 평균 계산
        for (int i = 0; i < sentenceVector.length; i++) {
            sentenceVector[i] /= keywords.length;
        }

        // 코사인 유사도 계산
        return cosineSimilarity(sentenceVector, wordVector);
    }


    // text에서 품사 태그를 포함하는 word를 keyword로 추출
    public String[] extractKeywords(String text, Set<String> posTags) {
        Annotation document = new Annotation(text.toLowerCase());
        pipeline.annotate(document);
        Set<String> keywords = new HashSet<>();

        for (CoreMap sentenceAnnotation : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : sentenceAnnotation.get(CoreAnnotations.TokensAnnotation.class)) {
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);

                if (posTags.contains(pos)) {
                    keywords.add(lemma);
                }
            }
        }
        return keywords.toArray(new String[0]);
    }

    // 품사 태그 생성
    public Set<String> createPosTags(List<String> posTags) {
        return new HashSet<>(posTags);
    }

    // 단어 벡터
    public double[] getWordVector(String word) {
        return word2Vec.getWordVector(word);
    }

    // 두 벡터간 코사인 유사도 계산
    public double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        // 0으로 나누기 방지
        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }*/
}
