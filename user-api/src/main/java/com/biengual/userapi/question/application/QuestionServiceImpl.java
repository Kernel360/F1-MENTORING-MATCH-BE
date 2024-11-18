package com.biengual.userapi.question.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.domain.document.question.QuestionDocument;
import com.biengual.core.enums.PointReason;
import com.biengual.userapi.point.domain.PointManager;
import com.biengual.userapi.question.domain.QuestionCommand;
import com.biengual.userapi.question.domain.QuestionInfo;
import com.biengual.userapi.question.domain.QuestionReader;
import com.biengual.userapi.question.domain.QuestionService;
import com.biengual.userapi.question.domain.QuestionStore;
import com.biengual.userapi.questionhistory.domain.QuestionHistoryStore;
import com.biengual.userapi.validator.QuestionValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionReader questionReader;
    private final QuestionStore questionStore;
    private final QuestionHistoryStore questionHistoryStore;
    private final QuestionValidator questionValidator;
    private final PointManager pointManager;

    @Override
    @Transactional
    public void createQuestion(Long contentId) {
        questionStore.createQuestion(contentId);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionInfo.DetailInfo getQuestions(QuestionCommand.GetQuestion command) {
        return QuestionInfo.DetailInfo.of(
            questionReader.findQuestionsByContentId(command.contentId(), command.userId())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionInfo.DetailInfo getCorrectedQuestions(QuestionCommand.GetQuestion command) {
        return QuestionInfo.DetailInfo.of(
            questionReader.findCorrectedQuestionsByContentId(command.contentId(), command.userId())
        );
    }

    @Override
    @Transactional
    public boolean verifyAnswer(QuestionCommand.Verify command) {
        QuestionDocument question = questionReader.findQuestionByQuestionId(command.questionId());

        // 유저의 answer 가 정답인지 확인
        boolean isCorrect = questionValidator.verifyAnswerOfQuestion(command.answer(), question);

        // 포인트 지급을 위해 이미 맞춘 문제인지 검증
        boolean alreadyCorrected =
            questionValidator.verifyAlreadyCorrectedQuestion(command.userId(), command.questionId());

        // 포인트 지급
        if (isCorrect && alreadyCorrected) {
            pointManager.updateAndSavePoint(PointReason.QUIZ_CORRECT_ANSWER, command.userId());
        }

        // 대시 보드 를 위해 문제 기록 저장
        questionHistoryStore.recordQuestionHistory(command.userId(), command.questionId(), isCorrect);

        return isCorrect;
    }

    @Override
    @Transactional
    public QuestionInfo.Hint getHint(QuestionCommand.GetHint command) {
        // 문제 힌트 조회는 포인트 중복 지불 방지 없음
        pointManager.updateAndSavePoint(PointReason.VIEW_QUIZ_HINT, command.userId());

        return questionReader.findHintOfQuestion(command.questionId());
    }
}