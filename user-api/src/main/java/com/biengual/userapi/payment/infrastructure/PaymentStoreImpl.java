package com.biengual.userapi.payment.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.paymenthistory.PaymentContentHistoryEntity;
import com.biengual.userapi.payment.domain.PaymentRepository;
import com.biengual.userapi.payment.domain.PaymentStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class PaymentStoreImpl implements PaymentStore {
    private final PaymentRepository paymentRepository;

    @Override
    public void updatePaymentHistory(Long userId, Long contentId) {
        paymentRepository.save(PaymentContentHistoryEntity.createPaymentHistory(userId, contentId));
    }
}
