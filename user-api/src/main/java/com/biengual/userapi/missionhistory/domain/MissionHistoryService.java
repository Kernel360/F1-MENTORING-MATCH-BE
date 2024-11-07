package com.biengual.userapi.missionhistory.domain;

import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;

public interface MissionHistoryService {
    MissionHistoryInfo.RecentHistories getRecentHistory(OAuth2UserPrincipal principal);
}
