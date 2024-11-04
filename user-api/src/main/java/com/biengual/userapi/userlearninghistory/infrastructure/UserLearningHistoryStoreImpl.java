package com.biengual.userapi.userlearninghistory.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.userlearninghistory.UserLearningHistoryEntity;
import com.biengual.userapi.userlearninghistory.domain.UserLearningHistoryRepository;
import com.biengual.userapi.userlearninghistory.domain.UserLearningHistoryStore;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@DataProvider
@RequiredArgsConstructor
public class UserLearningHistoryStoreImpl implements UserLearningHistoryStore {
    private final UserLearningHistoryRepository userLearningHistoryRepository;

    // TODO: 디테일 조회할 때부터 기록이 쌓이는게 아니라면 로직 수정 필요할 수도 있음
    // 학습 내역 쌓기
    @Override
    public void recordContentLearning(Long userId, Long contentId, BigDecimal learningRate) {
        UserLearningHistoryEntity userLearningHistory =
            userLearningHistoryRepository.findByUserIdAndContentId(userId, contentId)
                .orElseGet(() -> UserLearningHistoryEntity.createNewHistory(userId, contentId));

        userLearningHistory.record(learningRate);

        userLearningHistoryRepository.save(userLearningHistory);
    }
}
