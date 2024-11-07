package com.biengual.userapi.missionhistory.domain;

public interface MissionHistoryReader {
    MissionHistoryInfo.RecentHistories getRecentHistory(Long userId);
}
