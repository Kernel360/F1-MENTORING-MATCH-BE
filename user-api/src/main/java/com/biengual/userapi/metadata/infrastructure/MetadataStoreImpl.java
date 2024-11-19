package com.biengual.userapi.metadata.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.content.ContentLevelFeedbackDataMart;
import com.biengual.core.domain.entity.metadata.AggregationMetadataEntity;
import com.biengual.core.util.PeriodUtil;
import com.biengual.core.util.TimeRange;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentLevelFeedbackDataMartCustomRepository;
import com.biengual.userapi.content.domain.ContentLevelFeedbackDataMartRepository;
import com.biengual.userapi.content.domain.ContentLevelFeedbackHistoryCustomRepository;
import com.biengual.userapi.metadata.domain.AggregationMetadataRepository;
import com.biengual.userapi.metadata.domain.MetadataStore;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;

import static com.biengual.core.constant.ServiceConstant.CONTENT_LEVEL_FEEDBACK_HISTORY_TABLE;

@DataProvider
@RequiredArgsConstructor
public class MetadataStoreImpl implements MetadataStore {
    private final AggregationMetadataRepository aggregationMetadataRepository;
    private final ContentLevelFeedbackHistoryCustomRepository contentLevelFeedbackHistoryCustomRepository;
    private final ContentLevelFeedbackDataMartRepository contentLevelFeedbackDataMartRepository;
    private final ContentLevelFeedbackDataMartCustomRepository contentLevelFeedbackDataMartCustomRepository;

    // ContentLevelFeedbackHistory 집계
    @Override
    public void aggregateContentLevelFeedbackHistory() {
        AggregationMetadataEntity aggregationMetadata =
            this.findAggregationMetadata(CONTENT_LEVEL_FEEDBACK_HISTORY_TABLE);

        LocalDateTime aggregateEndTime = aggregationMetadata.getAggregateEndTime();

        Queue<TimeRange> aggregationPeriodQueue = PeriodUtil.getAggregationPeriodQueue(aggregationMetadata);

        // TODO: 현재는 순차 처리이고, 추후 개선하여 성능 비교하면 좋을 것 같습니다.
        // 각 집계 기간
        for (TimeRange timeRange : aggregationPeriodQueue) {
            List<ContentInfo.AggregatedLevelFeedback> aggregatedLevelFeedbackList =
                contentLevelFeedbackHistoryCustomRepository.countContentLevelsGroupByContentIdInTimeRange(timeRange);

            // 각 집계된 Content
            for (ContentInfo.AggregatedLevelFeedback aggregatedLevelFeedback : aggregatedLevelFeedbackList) {
                if (!contentLevelFeedbackDataMartRepository.existsById(aggregatedLevelFeedback.contentId())) {
                    ContentLevelFeedbackDataMart contentLevelFeedbackDataMart =
                        aggregatedLevelFeedback.toContentLevelFeedbackDataMart();

                    contentLevelFeedbackDataMartRepository.save(contentLevelFeedbackDataMart);
                } else {
                    contentLevelFeedbackDataMartCustomRepository
                        .updateByAggregatedLevelFeedbackInfo(aggregatedLevelFeedback);
                }
            }

            aggregateEndTime = timeRange.end();
        }

        aggregationMetadata.updateAggregateEndTime(aggregateEndTime);
    }

    // Internal Method =================================================================================================

    // TableName에 맞는 집계 메타데이터를 얻는 메소드
    private AggregationMetadataEntity findAggregationMetadata(String tableName) {
        return aggregationMetadataRepository.findByTableName(tableName)
            .orElseGet(() -> {
                AggregationMetadataEntity aggregationMetadata =
                    AggregationMetadataEntity.createEntityByTableName(tableName);

                return aggregationMetadataRepository.save(aggregationMetadata);
            });
    }
}
