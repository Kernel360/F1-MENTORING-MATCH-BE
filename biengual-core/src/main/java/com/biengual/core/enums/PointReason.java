package com.biengual.core.enums;

import lombok.Getter;

@Getter
public enum PointReason {
    // +
    FIRST_SIGN_UP(100L),
    DAILY_QUIZ(5L),
    DAILY_MISSION(5L),
    DAILY_CONTENT(5L),
    FIRST_DAILY_LOG_IN(10L),
    QUIZ_CORRECT_ANSWER(5L),

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
