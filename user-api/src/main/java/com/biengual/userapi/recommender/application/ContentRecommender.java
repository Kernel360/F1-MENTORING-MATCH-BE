package com.biengual.userapi.recommender.application;

import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.recommender.domain.CategoryLearningProgressCustomRepository;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.recommender.domain.RecommenderReader;
import com.biengual.userapi.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// TODO: CategoryLearningProgress 벡터화 역할을 누가 가져갈 것인가?
// TODO: Cosine Similarity 계산 역할은 누가 가져갈 것인가?
// TODO: 어떤 조건에 어떤 Recommender를 사용할 것인가?
// TODO: 캐싱을 할 것인가?
// TODO: 학습할 때 업데이트는 어떻게 할 것인가?
@Component
@RequiredArgsConstructor
public class ContentRecommender {
    private final RecommenderReader recommenderReader;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryLearningProgressCustomRepository categoryLearningProgressCustomRepository;

    public void recommend(Long userId) {
        long categoryCount = categoryRepository.count();
        long userCount = userRepository.count();
        boolean hasLearning = categoryLearningProgressCustomRepository.existsByUserId(userId);

        if (categoryCount >= 2 && userCount >= 5 && hasLearning) {
            RecommenderInfo.ContentRecommenderMetric contentRecommenderMetric =
                getContentRecommenderVector((int) categoryCount + 1);

            Map<Long, Double> similarityMap = new HashMap<>();
            Map<Long, Long[]> vectorMap = contentRecommenderMetric.vectorMap();

            Long[] targetUserVector = vectorMap.get(userId);

            for (Map.Entry<Long, Long[]> entry : vectorMap.entrySet()) {

                if (Objects.equals(entry.getKey(), userId)) continue;

                double similarity = calculateCosineSimilarity(targetUserVector, entry.getValue());

                similarityMap.put(entry.getKey(), similarity);
            }

            List<Long> similarUserList = similarityMap.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                .limit(2)
                .toList()
                .stream()
                .map(Map.Entry::getKey)
                .toList();
        }
    }

    // Internal Method =================================================================================================
    private RecommenderInfo.ContentRecommenderMetric getContentRecommenderVector(int vectorSize) {
        return recommenderReader.findContentRecommenderVector(vectorSize);
    }

    // 두 벡터간 코사인 유사도 계산
    private double calculateCosineSimilarity(Long[] vectorA, Long[] vectorB) {
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
    }
}
