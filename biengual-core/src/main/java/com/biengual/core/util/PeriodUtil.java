package com.biengual.core.util;

import java.time.LocalDateTime;
import java.time.YearMonth;

public class PeriodUtil {

    public static LocalDateTime getStartOfMonth(LocalDateTime localDateTime) {
        return YearMonth.of(localDateTime.getYear(), localDateTime.getMonth()).atDay(1).atStartOfDay();
    }

    public static LocalDateTime getEndOfMonth(LocalDateTime localDateTime) {
        return YearMonth.of(localDateTime.getYear(), localDateTime.getMonth()).atEndOfMonth()
            .atTime(23, 59, 59, 999_999);
    }
}
