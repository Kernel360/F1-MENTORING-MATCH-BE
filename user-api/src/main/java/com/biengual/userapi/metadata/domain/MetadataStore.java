package com.biengual.userapi.metadata.domain;

import java.util.Set;

/**
 * Metadata 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface MetadataStore {
    Set<Long> aggregateContentLevelFeedbackHistory();
}
