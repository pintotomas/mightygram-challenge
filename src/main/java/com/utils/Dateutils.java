package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Dateutils {

    public static String toTimeStamp(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return localDateTime.format(formatter);
    }
}
