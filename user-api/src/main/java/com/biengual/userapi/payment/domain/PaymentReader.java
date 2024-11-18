package com.biengual.userapi.payment.domain;

public interface PaymentReader {
    boolean existsPaymentContentHistory(Long userId, Long contentId);
}
