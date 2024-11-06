package com.biengual.userapi.payment.domain;

public interface PaymentStore {
    void updatePaymentHistory(Long userId, Long contentId);
}
