package com.biengual.userapi.schedule.application;

import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.enums.ContentType;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentStore;
import com.biengual.userapi.crawling.domain.CrawlingReader;
import com.biengual.userapi.crawling.domain.CrawlingStore;
import com.biengual.userapi.metadata.domain.MetadataStore;
import com.biengual.userapi.mission.domain.MissionStore;
import com.biengual.userapi.missionhistory.domain.MissionHistoryStore;
import com.biengual.userapi.question.domain.QuestionStore;
import com.biengual.userapi.recommender.domain.RecommenderStore;
import com.biengual.userapi.schedule.domain.ScheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final MissionStore missionStore;
    private final MissionHistoryStore missionHistoryStore;
    private final MetadataStore metadataStore;
    private final ContentStore contentStore;
    private final CrawlingStore crawlingStore;
    private final RecommenderStore recommenderStore;
    private final CrawlingReader crawlingReader;
    private final QuestionStore questionStore;

    /**
     * 미션 리셋 : 04:00 기준
     */
    @Override
    @Transactional
    @Scheduled(cron = "00 00 04 * * *")
    public void scheduleResetMission() {
        missionHistoryStore.saveMissionsBeforeReset();
        missionStore.resetMission();
    }

    // TODO: 나중에 같은 시간에 실행하는 집계 관련 스케줄러는
    //  aggregationMetadata의 모든 데이터를 가져와 한번에 실행할 수 있을 것 같습니다.
    // TODO: 집계와 반영은 같은 트랜잭션으로 봐야하나?
    @Override
    @Transactional
    @Scheduled(cron = "00 00 04 * * *")
    public void aggregateContentLevelFeedback() {
        Set<Long> aggregatedContentIdSet = metadataStore.aggregateContentLevelFeedbackHistory();
        contentStore.reflectContentLevel(aggregatedContentIdSet);
    }

    /**
     * 북마크 추천 시스템 업데이트 : 매주 월요일 00시 03분
     */
    @Override
    @Transactional
    @Scheduled(cron = "00 00 00 * * MON")
    public void scheduleUpdateLastWeekPopularBookmark() {
        recommenderStore.createLastWeekBookmarkRecommender();
    }

    @Override
    @Transactional
    @Scheduled(cron = "00 00 04 * * *")
    public void scheduleCrawling() {
        // 1. 크롤링 할 컨텐츠 확인
        List<ContentCommand.CrawlingContent> commands = crawlingReader.getDailyUrlsForCrawling();

        for (ContentCommand.CrawlingContent command : commands) {
            // 2. 해당 url 에 대해 컨텐츠 타입에 따른 크롤링
            ContentCommand.Create createContentCommand =
                command.contentType().equals(ContentType.READING) ?
                    crawlingStore.getCNNDetail(command) :
                    crawlingStore.getYoutubeDetail(command);

            // 3. Content 저장
            Long contentId = contentStore.createContent(createContentCommand);

            // 4. Question 생성 및 저장
            questionStore.createQuestion(contentId);
        }
    }
}
