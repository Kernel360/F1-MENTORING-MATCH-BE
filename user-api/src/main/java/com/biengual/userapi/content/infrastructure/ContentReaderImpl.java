package com.biengual.userapi.content.infrastructure;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.content.repository.ContentCustomRepository;
import com.biengual.userapi.util.PaginationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@DataProvider
@RequiredArgsConstructor
public class ContentReaderImpl implements ContentReader {
    private final ContentCustomRepository contentCustomRepository;

    // 스크랩 많은 순 컨텐츠 조회
    @Override
    public List<ContentInfo.PreviewContent> findContentsByScrapCount(Integer size) {
        return contentCustomRepository.findContentsByScrapCount(size);
    }

    // 검색 조건에 맞는 컨텐츠 조회
    @Override
    public PaginationInfo<ContentInfo.PreviewContent> findPageBySearch(ContentCommand.Search command) {
        Page<ContentInfo.PreviewContent> page =
            contentCustomRepository.findPageBySearch(command.pageable(), command.keyword());

        return PaginationInfo.from(page);
    }
}
