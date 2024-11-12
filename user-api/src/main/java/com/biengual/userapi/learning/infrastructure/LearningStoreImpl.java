package com.biengual.userapi.learning.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.domain.entity.learning.UserLearningHistoryEntity;
import com.biengual.userapi.learning.domain.*;
import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class LearningStoreImpl implements LearningStore {
    private final LearningHistoryRepository learningHistoryRepository;
    private final UserLearningHistoryRepository userLearningHistoryRepository;
    private final CategoryLearningHistoryRepository categoryLearningHistoryRepository;

    // 모든 학습 내역 쌓기
    @Override
    public void recordLearningHistory(LearningCommand.RecordLearningRate command) {
        learningHistoryRepository.save(command.toLearningHistoryEntity());
    }

    // TODO: Validate를 앞단에서 하는 것이 좋은가? Validator 클래스를 만드는 것이 좋은가?
    // TODO: 최근 검색을 위한 최근 학습 히스토리에 저장하는 로직이 변경될 수 있음
    // 최근 학습 내역 쌓기
    @Override
    public void recordRecentLearningHistory(LearningCommand.RecordLearningRate command) {
        UserLearningHistoryEntity userLearningHistory =
            userLearningHistoryRepository.findByUserIdAndContentId(command.userId(), command.contentId())
                .map(history -> {
                    history.record(command.learningRate());
                    return history;
                })
                .orElseGet(command::toUserLearningHistoryEntity);

        userLearningHistoryRepository.save(userLearningHistory);
    }

    // 카테고리별 학습 내역 쌓기
    @Override
    public void recordCategoryLearningHistory(LearningCommand.RecordLearningRate command, ContentEntity content) {
        Long categoryId = content.getCategory().getId();

        categoryLearningHistoryRepository.save(command.toCategoryLearningHistoryEntity(categoryId));
    }
}
