package com.biengual.userapi.pointdatamart.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.pointdatamart.PointDataMart;

public interface PointDataMartRepository extends JpaRepository<PointDataMart, Long> {
    Optional<PointDataMart> findByUserId(Long userId);
}
