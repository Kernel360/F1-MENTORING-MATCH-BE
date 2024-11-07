package com.biengual.userapi.missionhistory.application;

import org.springframework.stereotype.Service;

import com.biengual.userapi.missionhistory.domain.MissionHistoryInfo;
import com.biengual.userapi.missionhistory.domain.MissionHistoryReader;
import com.biengual.userapi.missionhistory.domain.MissionHistoryService;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionHistoryServiceImpl implements MissionHistoryService {
    private final MissionHistoryReader missionHistoryReader;

    @Override
    public MissionHistoryInfo.RecentHistories getRecentHistory(OAuth2UserPrincipal principal) {
        return missionHistoryReader.getRecentHistory(principal.getId());
    }
}
