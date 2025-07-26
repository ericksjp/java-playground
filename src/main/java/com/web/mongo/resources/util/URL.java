package com.web.mongo.resources.util;

import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class URL {

    public static String decodeURL(String rawUrl) {
        try {
            return URLDecoder.decode(rawUrl, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    public static LocalDate decodeLocalDate(String date, LocalDate defaultValue) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }
}
