package com.biengual.userapi.recommender.application;

import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.recommender.domain.RecommenderReader;
import com.biengual.userapi.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// TODO: CategoryLearningProgress 벡터화 역할을 누가 가져갈 것인가?
// TODO: Cosine Similarity 계산 역할은 누가 가져갈 것인가?
// TODO: 어떤 조건에 어떤 Recommender를 사용할 것인가?
@Component
@RequiredArgsConstructor
public class ContentRecommender {
    private final RecommenderReader recommenderReader;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public void recommend() {
        long categoryCount = categoryRepository.count();
        long userCount = userRepository.count();

        if (categoryCount >= 2 && userCount >= 5) {
            RecommenderInfo.ContentRecommenderVector contentRecommenderVector =
                getContentRecommenderVector((int) categoryCount + 1);
        }
    }

    // Internal Method =================================================================================================
    private RecommenderInfo.ContentRecommenderVector getContentRecommenderVector(int vectorSize) {
        return recommenderReader.findContentRecommenderVector(vectorSize);
    }

}
