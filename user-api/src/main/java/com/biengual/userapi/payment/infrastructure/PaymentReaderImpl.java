package com.biengual.userapi.payment.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.payment.domain.PaymentReader;
import com.biengual.userapi.payment.domain.PaymentContentHistoryRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class PaymentReaderImpl implements PaymentReader {
    private final PaymentContentHistoryRepository paymentContentHistoryRepository;

    @Override
    public boolean existsPaymentContentHistory(Long userId, Long contentId) {
        return paymentContentHistoryRepository.existsByUserIdAndContentId(userId, contentId);
    }
}
