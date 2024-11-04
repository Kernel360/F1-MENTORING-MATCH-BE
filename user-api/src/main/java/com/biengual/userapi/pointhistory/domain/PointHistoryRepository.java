package com.biengual.userapi.pointhistory.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.pointhistory.PointHistoryEntity;

public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {
    // BatchScheduler 에서 사용하는 메서드
    Slice<PointHistoryEntity> findByProcessedFalse(PageRequest pageRequest1, PageRequest pageRequest2);
}
