package com.biengual.userapi.learning.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.domain.entity.learning.RecentLearningHistoryEntity;
import com.biengual.userapi.learning.domain.*;
import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class LearningStoreImpl implements LearningStore {
    private final LearningHistoryRepository learningHistoryRepository;
    private final LearningHistoryCustomRepository learningHistoryCustomRepository;
    private final RecentLearningHistoryRepository recentLearningHistoryRepository;
    private final CategoryLearningHistoryRepository categoryLearningHistoryRepository;

    // 모든 학습 내역 쌓기
    @Override
    public void recordLearningHistory(LearningCommand.RecordLearningRate command) {
        learningHistoryRepository.save(command.toLearningHistoryEntity());
    }

    // TODO: Validate를 앞단에서 하는 것이 좋은가? Validator 클래스를 만드는 것이 좋은가?
    //  -> 현재는 앞단에서 ContentReader에서 검증한 content를 반환
    // TODO: 동시성 보장을 할까요? 말까요?
    // 최근 학습 내역 쌓기
    @Override
    public void recordRecentLearningHistory(LearningCommand.RecordLearningRate command) {
        RecentLearningHistoryEntity userLearningHistory =
            recentLearningHistoryRepository.findByUserIdAndContentId(command.userId(), command.contentId())
                .map(history -> {
                    history.record(command.learningRate());
                    return history;
                })
                .orElseGet(command::toUserLearningHistoryEntity);

        recentLearningHistoryRepository.save(userLearningHistory);
    }

    // 카테고리별 학습 내역 쌓기
    @Override
    public void recordCategoryLearningHistory(LearningCommand.RecordLearningRate command, ContentEntity content) {
        Long categoryId = content.getCategory().getId();

        if (validateAlreadyLearningInMonth(command)) {
            return;
        }

        categoryLearningHistoryRepository.save(command.toCategoryLearningHistoryEntity(categoryId));
    }

    // Internal Method =================================================================================================

    private boolean validateAlreadyLearningInMonth(LearningCommand.RecordLearningRate command) {
        return learningHistoryCustomRepository
            .existsByUserIdAndContentIdInMonth(command.userId(), command.contentId(), command.learningTime());
    }
}
