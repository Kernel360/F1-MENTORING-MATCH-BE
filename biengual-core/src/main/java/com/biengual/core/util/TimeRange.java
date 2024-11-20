package com.biengual.core.util;

import com.biengual.core.response.error.exception.CommonException;
import lombok.Builder;

import java.time.LocalDateTime;

import static com.biengual.core.response.error.code.ServerError.TIME_RANGE_IS_INVALID;

@Builder
public record TimeRange(
    LocalDateTime start,
    LocalDateTime end
) {
    public TimeRange {
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new CommonException(TIME_RANGE_IS_INVALID);
        }
    }

    public static TimeRange of(LocalDateTime start, LocalDateTime end) {
        return TimeRange.builder()
            .start(start)
            .end(end)
            .build();
    }
}
