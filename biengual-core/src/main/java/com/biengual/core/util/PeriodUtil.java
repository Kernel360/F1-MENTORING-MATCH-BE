package com.biengual.core.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

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
}
