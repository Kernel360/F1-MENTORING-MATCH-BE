package com.biengual.userapi.content.infrastructure;

import static com.biengual.core.response.error.code.ContentErrorCode.*;

import java.util.List;

import org.bson.types.ObjectId;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentDocumentReader;
import com.biengual.userapi.content.domain.ContentDocumentRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class ContentDocumentReaderImpl implements ContentDocumentReader {
    private final ContentDocumentRepository contentDocumentRepository;

    // TODO: 추후 ContentDocument를 다 불러오는 것이 아닌 scripts만 가져올 수 있도록 개선해볼
    // 디테일 컨텐츠 script 조회
    @Override
    public List<Script> findScripts(String contentDocumentId) {
        ContentDocument contentDocument =
            contentDocumentRepository.findContentDocumentById(new ObjectId(contentDocumentId))
                .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

        return contentDocument.getScripts();
    }
}
