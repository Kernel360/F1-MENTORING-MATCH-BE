package com.biengual.core.enums;

import lombok.Getter;

@Getter
public enum PointReason {
    // +
    FIRST_SIGN_UP(100),
    DAILY_QUIZ(7),
    DAILY_MISSION(8),
    DAILY_CONTENT(9),
    FIRST_DAILY_LOG_IN(10),

    // -
    VIEW_RECENT_CONTENT(-10),
    VIEW_QUIZ_HINT(-5);

    private final int value;

    PointReason(int value) {
        this.value = value;
    }
}
