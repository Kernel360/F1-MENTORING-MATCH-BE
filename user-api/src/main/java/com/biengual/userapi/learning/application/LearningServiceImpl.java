package com.biengual.userapi.learning.application;

import com.biengual.userapi.learning.domain.LearningCommand;
import com.biengual.userapi.learning.domain.LearningService;
import com.biengual.userapi.learning.domain.LearningStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {
    private final LearningStore learningStore;

    // 학습률 업데이트
    @Override
    public void updateLearningRate(LearningCommand.UpdateLearningRate command) {
        learningStore.recordContentLearning(command);
    }
}
