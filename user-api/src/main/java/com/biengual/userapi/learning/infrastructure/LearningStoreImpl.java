package com.biengual.userapi.learning.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.domain.entity.userlearninghistory.UserLearningHistoryEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.learning.domain.LearningCommand;
import com.biengual.userapi.learning.domain.LearningStore;
import com.biengual.userapi.learning.domain.UserLearningHistoryRepository;
import lombok.RequiredArgsConstructor;

import static com.biengual.core.response.error.code.ContentErrorCode.CONTENT_IS_DEACTIVATED;
import static com.biengual.core.response.error.code.ContentErrorCode.CONTENT_NOT_FOUND;

@DataProvider
@RequiredArgsConstructor
public class LearningStoreImpl implements LearningStore {
    private final ContentRepository contentRepository;
    private final UserLearningHistoryRepository userLearningHistoryRepository;

    // TODO: 새로 생기는 학습 내역인 경우 recentLearningTime에 LocalDateTime.now()를 할당하는 작업을 2번하는데, 무시할만 한가?
    // TODO: Validate를 앞단에서 하는 것이 좋은가? Validator 클래스를 만드는 것이 좋은가?
    // 학습 내역 쌓기
    @Override
    public void recordContentLearning(LearningCommand.UpdateLearningRate command) {
        validateLearnableContent(command.contentId(), command.userId());

        UserLearningHistoryEntity userLearningHistory =
            userLearningHistoryRepository.findByUserIdAndContentId(command.userId(), command.contentId())
                .orElseGet(command::toUserLearningHistoryEntity);

        userLearningHistory.record(command.learningRate());

        userLearningHistoryRepository.save(userLearningHistory);
    }

    // Internal Method =================================================================================================

    private void validateLearnableContent(Long contentId, Long userId) {
        ContentEntity content = contentRepository.findById(contentId)
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
        if (content.getContentStatus().equals(ContentStatus.DEACTIVATED)) {
            throw new CommonException(CONTENT_IS_DEACTIVATED);
        }

        if (content.isRecentContent()) {
            // TODO: 최신 컨텐츠에 대해 포인트를 지불했는지에 대한 검증이 필요
        }
    }
}
