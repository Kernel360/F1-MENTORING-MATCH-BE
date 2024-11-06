package com.biengual.userapi.payment.domain;

public interface PaymentReader {
    boolean existsPaymentHistory(Long userId, Long contentId);
}
