package com.biengual.userapi.content.domain;

import java.util.List;

import com.biengual.core.domain.document.content.script.Script;

/**
 * Content 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface ContentDocumentReader {
    List<Script> findScripts(String contentDocumentId);
}
