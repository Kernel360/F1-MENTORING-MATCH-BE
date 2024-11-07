package com.biengual.userapi.missionhistory.domain;

public interface MissionHistoryReader {
    MissionHistoryInfo.RecentHistories findRecentHistory(Long userId);
}
