package com.loong.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

@Slf4j
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @Override
    public LocalDateTime convert(String value) {
        log.info("原来的值为:{}", value);
//      2024-05-05T16:00:00.000Z
        if (value.contains("Z") && value.contains("T")) {

            int year = Integer.parseInt(value.substring(0, 4));
            int month = Integer.parseInt(value.substring(5, 7));
            int day = Integer.parseInt(value.substring(8, 10));
            return LocalDateTime.of(year, month, day, 0, 0, 0);
        }
        return LocalDateTime.now();
    }
}
