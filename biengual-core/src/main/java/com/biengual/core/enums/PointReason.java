package com.biengual.core.enums;

import lombok.Getter;

@Getter
public enum PointReason {
    // TODO: 로직 모두 확인되면 포인트 값 수정해야 함

    // +
    FIRST_SIGN_UP(100L),
    DAILY_QUIZ(7L),
    DAILY_MISSION(8L),
    DAILY_CONTENT(9L),
    FIRST_DAILY_LOG_IN(10L),
    QUIZ_CORRECT_ANSWER(5L),    // 일단 확정

    // -
    VIEW_RECENT_CONTENT(-10L),
    VIEW_QUIZ_HINT(-5L);

    private final Long value;

    PointReason(Long value) {
        this.value = value;
    }

    public Long add(Long point){
        return this.value + point;
    }
}
