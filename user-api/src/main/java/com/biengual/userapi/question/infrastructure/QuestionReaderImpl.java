package com.biengual.userapi.question.infrastructure;

import static com.biengual.core.response.error.code.ContentErrorCode.*;
import static com.biengual.core.response.error.code.QuestionErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.question.QuestionDocument;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.question.domain.QuestionDocumentRepository;
import com.biengual.userapi.question.domain.QuestionInfo;
import com.biengual.userapi.question.domain.QuestionReader;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class QuestionReaderImpl implements QuestionReader {
    private final QuestionDocumentRepository questionDocumentRepository;
    private final ContentRepository contentRepository;
    private final ContentDocumentRepository contentDocumentRepository;

    @Override
    public List<QuestionInfo.Detail> findQuestionsByContentId(Long contentId) {

        ContentDocument contentDocument = this.getContentDocument(contentId);
        List<String> questionDocumentIds = contentDocument.getQuestionIds();

        if (questionDocumentIds.isEmpty()) {
            throw new CommonException(QUESTION_NOT_FOUND);
        }

        List<QuestionInfo.Detail> questions = new ArrayList<>();

        for (String questionDocumentId : questionDocumentIds) {
            QuestionDocument questionDocument = questionDocumentRepository.findById(new ObjectId(questionDocumentId))
                .orElseThrow(() -> new CommonException(QUESTION_NOT_FOUND));
            questions.add(
                QuestionInfo.Detail.of(questionDocument)
            );
        }
        return questions;
    }

    @Override
    public QuestionDocument findQuestionByQuestionId(String questionId) {
        return questionDocumentRepository.findById(new ObjectId(questionId))
            .orElseThrow(() -> new CommonException(QUESTION_NOT_FOUND));
    }

    @Override
    public QuestionInfo.Hint findHintOfQuestion(String questionId) {
        return QuestionInfo.Hint.of(this.findQuestionByQuestionId(questionId).getHint());
    }

    // Internal Methods ------------------------------------------------------------------------------------------------
    private ContentEntity getContentEntity(Long contentId) {
        ContentEntity content = contentRepository.findById(contentId)
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

        if (!content.getContentStatus().equals(ContentStatus.ACTIVATED)) {
            throw new CommonException(CONTENT_IS_DEACTIVATED);
        }

        return content;
    }

    private ContentDocument getContentDocument(Long contentId) {
        ContentEntity content = getContentEntity(contentId);

        return contentDocumentRepository.findById(new ObjectId(content.getMongoContentId()))
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
    }
}