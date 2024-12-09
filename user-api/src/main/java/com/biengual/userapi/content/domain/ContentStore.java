package com.biengual.userapi.content.domain;

import java.util.Set;

/**
 * Content 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface ContentStore {
    Long createContent(ContentCommand.Create command);

    void modifyContentStatus(Long contentId);

    void increaseHits(Long contentId);

    void recordContentLevelFeedbackHistory(ContentCommand.SubmitLevelFeedback command);

    void reflectContentLevel(Set<Long> contentIdSet);

    void initializeOpenSearch();

    void delete();
}
