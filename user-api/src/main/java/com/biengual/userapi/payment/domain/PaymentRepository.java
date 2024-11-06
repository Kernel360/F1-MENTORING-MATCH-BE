package com.biengual.userapi.payment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.usercontenthistory.PaymentContentHistoryEntity;

public interface PaymentRepository extends JpaRepository<PaymentContentHistoryEntity, Long> {
    boolean existsByUserIdAndContentId(Long userId, Long contentId);
}
