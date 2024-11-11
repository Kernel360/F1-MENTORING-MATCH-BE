package com.biengual.userapi.missionhistory.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

public class MissionHistoryInfo {
    public record RecentHistory(
        LocalDateTime date,
        Integer count
    ) {
    }

    @Builder
    public record RecentHistories(
        List<RecentHistory> recentHistories
    ) {
        public static RecentHistories of(List<RecentHistory> recentHistories) {
            return RecentHistories.builder()
                .recentHistories(recentHistories)
                .build();
        }
    }
}
