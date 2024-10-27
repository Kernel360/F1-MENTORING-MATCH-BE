package com.biengual.userapi.content.domain;

import com.biengual.userapi.script.domain.entity.Script;

import java.util.List;

/**
 * Content 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface ContentDocumentReader {
    List<Script> findScripts(String contentDocumentId);
}
