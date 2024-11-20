package com.biengual.userapi.recommender.infrastructure;

import static com.biengual.core.response.error.code.ContentErrorCode.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.bson.types.ObjectId;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.entity.recommender.BookmarkRecommenderEntity;
import com.biengual.core.domain.entity.recommender.CategoryRecommenderEntity;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.bookmark.domain.BookmarkCustomRepository;
import com.biengual.userapi.category.domain.CategoryCustomRepository;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.recommender.domain.BookmarkRecommenderRepository;
import com.biengual.userapi.recommender.domain.CategoryRecommenderRepository;
import com.biengual.userapi.recommender.domain.RecommenderCustomRepository;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.recommender.domain.RecommenderStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class RecommenderStoreImpl implements RecommenderStore {
    private final CategoryRecommenderRepository categoryRecommenderRepository;
    private final BookmarkRecommenderRepository bookmarkRecommenderRepository;
    private final CategoryCustomRepository categoryCustomRepository;
    private final RecommenderCustomRepository recommenderCustomRepository;
    private final BookmarkCustomRepository bookmarkCustomRepository;
    private final ContentDocumentRepository contentDocumentRepository;
    private final ContentCustomRepository contentCustomRepository;

    @Override
    public void createAndUpdateCategoryRecommender() {
        // CategoryRecommender 에 추가되지 않은 카테고리 있는지 확인 및 추가
        this.createdCategoryRecommender();

        // CategoryRecommender 의 similarCategoryIds 업데이트
        recommenderCustomRepository.updateCategoryRecommender();
    }

    @Override
    public void createLastWeekBookmarkRecommender() {
        LocalDate lastWeek =
            PeriodUtil.getFewWeeksAgo(LocalDate.from(LocalDateTime.now()), 1, DayOfWeek.MONDAY);

        LocalDateTime startOfWeek = PeriodUtil.getStartOfWeek(LocalDateTime.of(lastWeek, LocalTime.MIDNIGHT));
        LocalDateTime endOfWeek =
            PeriodUtil.getEndOfWeek(LocalDateTime.of(lastWeek, LocalTime.of(23, 59, 59)));

        // TODO : LISTENING 의 경우 문장이 쪼개진 상태로 저장이 되고 문장 자체도 완전한 상태가 아니기 때문에
        // TODO : 여기서 쓰고 싶으면 content create 과정에 추가적인 전처리가 필요함
        List<RecommenderInfo.VerifiedBookmark> bookmarksOfWeek =
            bookmarkCustomRepository.findPopularBookmarksOfReadingContentsOnWeek(startOfWeek, endOfWeek);

        for (RecommenderInfo.VerifiedBookmark info : bookmarksOfWeek) {
            Long contentId = info.contentId();
            // 번역 내용을 content document 로부터 가져옴
            // TODO : TranslateApiClient 로 다시 번역 하는 방식 보다 느리면 차라리 번역을 다시 하는 방식도 고려
            String koDetail = this.getContentDocument(contentId)
                .getScripts()
                .get(Math.toIntExact(info.sentenceIndex()))
                .getKoScript();

            // Bookmark Recommender 에 저장
            BookmarkRecommenderEntity recommender = BookmarkRecommenderEntity.createdByBookmark(
                info.contentId(), info.sentenceIndex(),
                info.enDetail(), koDetail,
                startOfWeek, endOfWeek
            );
            bookmarkRecommenderRepository.save(recommender);
        }
    }

    // Internal Methods=================================================================================================
    private void createdCategoryRecommender() {
        List<Long> notUpdatedCategoryIds = categoryCustomRepository.findAllCategoryIds()
            .stream()
            .filter(id -> !categoryRecommenderRepository.existsByCategoryId(id))
            .toList();

        if (!notUpdatedCategoryIds.isEmpty()) {
            List<CategoryRecommenderEntity> categoryRecommenderEntities =
                notUpdatedCategoryIds
                    .stream()
                    .map(CategoryRecommenderEntity::createdByCategoryId)
                    .toList();

            categoryRecommenderRepository.saveAll(categoryRecommenderEntities);
        }
    }

    private ContentDocument getContentDocument(Long scriptIndex) {
        String mongoId = contentCustomRepository.findMongoIdByContentId(scriptIndex);

        return contentDocumentRepository.findContentDocumentById(new ObjectId(mongoId))
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
    }
}