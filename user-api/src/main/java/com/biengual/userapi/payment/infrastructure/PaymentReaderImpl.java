package com.biengual.userapi.payment.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.payment.domain.PaymentReader;
import com.biengual.userapi.payment.domain.PaymentRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class PaymentReaderImpl implements PaymentReader {
    private final PaymentRepository paymentRepository;

    @Override
    public boolean existsPaymentHistory(Long userId, Long contentId) {
        return paymentRepository.existsByUserIdAndContentId(userId, contentId);
    }
}
