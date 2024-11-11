package com.biengual.userapi.missionhistory.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.missionhistory.domain.MissionHistoryCustomRepository;
import com.biengual.userapi.missionhistory.domain.MissionHistoryInfo;
import com.biengual.userapi.missionhistory.domain.MissionHistoryReader;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class MissionHistoryReaderImpl implements MissionHistoryReader {
    private final MissionHistoryCustomRepository missionHistoryCustomRepository;

    @Override
    public MissionHistoryInfo.RecentHistories findRecentHistory(Long userId) {
        return MissionHistoryInfo.RecentHistories.of(missionHistoryCustomRepository.findRecentMissionHistory(userId));
    }
}
