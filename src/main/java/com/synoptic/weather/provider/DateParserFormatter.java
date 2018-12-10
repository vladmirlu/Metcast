package com.synoptic.weather.provider;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Component to format data
 * */
@Component
public class DateParserFormatter {

    private static final Logger logger = Logger.getLogger(DateParserFormatter.class);

    /**
     * Date and time formatter
     */
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m");

    /**
     * Parses date string to LocalDateTime
     *
     * @param timeStr time as string
     * @param dateStr date as string
     * @return parsed local date time
     */
    public LocalDateTime parseStringToDateTime(String timeStr, String dateStr) {
        int time = Integer.parseInt(timeStr);
        logger.debug("Parsing time " + timeStr + " and date " + dateStr + " to java.LocalDateTime");
        return LocalDateTime.parse(dateStr + " " + time / 100 + ":" + time % 100, formatter);
    }

    /**
     * Formats LocalDateTime
     *
     * @param dateTime local date and time
     * @return formatted local date and time in format of "yyyy-MM-dd H:m"
     */
    public LocalDateTime formatDateTime(LocalDateTime dateTime) {
        logger.debug("Parsing LocalDateTime " + dateTime + " to format of " + formatter.toString());
        return LocalDateTime.parse(dateTime.format(formatter), formatter);
    }
}
