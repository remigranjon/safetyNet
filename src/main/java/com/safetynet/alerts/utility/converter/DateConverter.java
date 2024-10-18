package com.safetynet.alerts.utility.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static final Logger logger = LogManager.getLogger(DateConverter.class);

    public static Date convertStringToDate(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            logger.error("Error parsing date: {}", dateString, e);
            return null;
        }
    }
}
