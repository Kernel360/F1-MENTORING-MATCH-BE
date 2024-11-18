package com.biengual.userapi.learning.application;

import com.biengual.core.annotation.RedisDistributedLock;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.learning.domain.LearningCommand;
import com.biengual.userapi.learning.domain.LearningService;
import com.biengual.userapi.learning.domain.LearningStore;
import com.biengual.userapi.validator.LearningValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {
    private final LearningValidator learningValidator;
    private final ContentReader contentReader;
    private final LearningStore learningStore;

    // TODO: 카테고리 학습 내역 테이블 변경이 필요해 보임 (contentId 컬럼 추가)
    //  그리고 학습 내역이 기록이 되면 최근 학습 내역과 카테고리별 학습 내역을 비동기적으로 기록할 수 있을 것 같습니다.
    // 학습률 업데이트
    @Override
    @RedisDistributedLock(key = "#command.userId() + \":\" + #command.contentId()")
    public void recordLearningRate(LearningCommand.RecordLearningRate command) {
        ContentEntity content = contentReader.findLearnableContent(command.contentId(), command.userId());

        if (!learningValidator.verifyAlreadyLearningInMonth(
            command.userId(), command.contentId(), command.learningTime())
        ) {
            learningStore.recordCategoryLearningHistory(command, content);
        }

        learningStore.recordLearningHistory(command);

        learningStore.recordRecentLearningHistory(command);
    }
}
