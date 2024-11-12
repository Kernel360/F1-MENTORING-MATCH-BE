package com.biengual.userapi.learning.application;

import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.learning.domain.LearningCommand;
import com.biengual.userapi.learning.domain.LearningService;
import com.biengual.userapi.learning.domain.LearningStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {
    private final ContentReader contentReader;
    private final LearningStore learningStore;

    // 학습률 업데이트
    @Override
    @Transactional
    public void recordLearningRate(LearningCommand.RecordLearningRate command) {
        ContentEntity content = contentReader.findLearnableContent(command.contentId(), command.userId());

        learningStore.recordLearningHistory(command);

        learningStore.recordRecentLearningHistory(command);

        learningStore.recordCategoryLearningHistory(command, content);
    }
}
