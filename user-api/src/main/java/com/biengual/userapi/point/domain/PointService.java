package com.biengual.userapi.point.domain;

/**
 * Point 도메인의 Service 계층의 인터페이스
 */
public interface PointService {
    void payPointsForRecentContent(Long contentId, Long userId);
}
