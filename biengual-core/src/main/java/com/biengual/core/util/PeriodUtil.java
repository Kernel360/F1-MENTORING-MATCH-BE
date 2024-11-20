package com.biengual.core.util;

import com.biengual.core.domain.entity.metadata.AggregationMetadataEntity;
import com.biengual.core.enums.IntervalType;
import com.biengual.core.response.error.exception.CommonException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayDeque;
import java.util.Queue;

import static com.biengual.core.response.error.code.ServerError.INTERVAL_TYPE_IS_INVALID;

/**
 * 쿼리에 사용하는 날짜별 조회를 위한 Util 클래스
 *
 * @author 문찬욱
 */
public class PeriodUtil {

    public static YearMonth toYearMonth(String date) {
        if (date == null) {
            return YearMonth.now();
        }

        return YearMonth.parse(date);
    }

    public static LocalDate toLocalDate(String date) {
        return (date == null) ? LocalDate.now() : LocalDate.parse(date);
    }

    public static YearMonth toYearMonth(LocalDateTime localDateTime) {
        return YearMonth.of(localDateTime.getYear(), localDateTime.getMonth());
    }

    public static LocalDateTime getStartOfMonth(YearMonth yearMonth) {
        return yearMonth.atDay(1).atStartOfDay();
    }

    public static LocalDateTime getEndOfMonth(YearMonth yearMonth) {
        return yearMonth.atEndOfMonth().atTime(23, 59, 59, 999_999);
    }

    public static LocalDate getFewWeeksAgo(LocalDate currentDate, long subtractWeek, DayOfWeek dayOfWeek) {
        return currentDate.minusWeeks(subtractWeek).with(TemporalAdjusters.previousOrSame(dayOfWeek));
    }

    public static Queue<TimeRange> getAggregationPeriodQueue(AggregationMetadataEntity aggregationMetadata) {
        Queue<TimeRange> aggregationPeriodQueue = new ArrayDeque<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = aggregationMetadata.getNextAggTime();
        LocalDateTime end = calculateAggregateEndTime(
            now, start, aggregationMetadata.getIntervalType(), aggregationMetadata.getIntervalNumber()
        );

        while (!now.isBefore(end) && !start.isEqual(end)) {
            TimeRange timeRange = TimeRange.of(start, end);

            aggregationPeriodQueue.add(timeRange);

            start = end;
            end = calculateAggregateEndTime(
                now, start, aggregationMetadata.getIntervalType(), aggregationMetadata.getIntervalNumber()
            );
        }

        return aggregationPeriodQueue;
    }

    // Internal Method =================================================================================================

    private static LocalDateTime calculateAggregateEndTime(LocalDateTime now, LocalDateTime start, IntervalType intervalType, Integer interval) {
        switch (intervalType) {
            case YEARLY:
                return start.plusYears(interval);
            case MONTHLY:
                return start.plusMonths(interval);
            case DAILY:
                return start.plusDays(interval);
            case HOURLY:
                return start.plusHours(interval);
            case ALL:
                return now.withMinute(0).withSecond(0).withNano(0);
            default:
                throw new CommonException(INTERVAL_TYPE_IS_INVALID);
        }
    }
}
