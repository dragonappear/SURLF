package me.dragonappear.domain.link.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormat {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss+SSSS";

    public static String getDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).toString();
    }
}
