package com.biengual.userapi.content.domain;

/**
 * Content 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface ContentStore {
	void createContent(ContentCommand.Create command);

	void modifyContentStatus(Long contentId);

	void increaseHits(Long contentId);
}
