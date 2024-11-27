package com.biengual.userapi.point.application;

import com.biengual.userapi.point.domain.PointService;
import org.springframework.stereotype.Service;

@Service
public class PointServiceImpl implements PointService {

    // 최신 컨텐츠에 대한 포인트 지불
    @Override
    public void payPointsForRecentContent(Long contentId, Long userId) {

    }
}
