package com.biengual.userapi.learning.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.userlearninghistory.UserLearningHistoryEntity;
import com.biengual.userapi.learning.domain.LearningCommand;
import com.biengual.userapi.learning.domain.LearningStore;
import com.biengual.userapi.learning.domain.UserLearningHistoryRepository;
import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class LearningStoreImpl implements LearningStore {
    private final UserLearningHistoryRepository userLearningHistoryRepository;

    // TODO: 새로 생기는 학습 내역인 경우 recentLearningTime에 LocalDateTime.now()를 할당하는 작업을 2번하는데, 무시할만 한가?
    // 학습 내역 쌓기
    @Override
    public void recordContentLearning(LearningCommand.UpdateLearningRate command) {
        UserLearningHistoryEntity userLearningHistory =
            userLearningHistoryRepository.findByUserIdAndContentId(command.userId(), command.contentId())
                .orElseGet(command::toUserLearningHistoryEntity);

        userLearningHistory.record(command.learningRate());

        userLearningHistoryRepository.save(userLearningHistory);
    }
}
