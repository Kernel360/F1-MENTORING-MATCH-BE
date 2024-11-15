package com.biengual.userapi.dashboard.presentation.dto;

import com.biengual.core.enums.PointReason;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class GetMonthlyPointHistoryDto {

    public record PointRecord(
        PointReason reason,
        Long point
    ) {
    }

    @Builder
    public record DailyPointHistory(
        LocalDate date,
        List<PointRecord> pointsHistory
    ) {
    }

    @Builder
    public record Response(
        Long currentPoint,
        List<DailyPointHistory> monthlyHistoryList
    ) {
    }
}
