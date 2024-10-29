package com.biengual.userapi.content.infrastructure;

import com.biengual.userapi.core.annotation.DataProvider;
import com.biengual.userapi.core.domain.document.content.ContentDocument;
import com.biengual.userapi.content.domain.ContentDocumentReader;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.core.response.error.exception.CommonException;
import com.biengual.userapi.core.domain.document.content.script.Script;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

import static com.biengual.userapi.core.response.error.code.ContentErrorCode.CONTENT_NOT_FOUND;

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
