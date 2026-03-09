package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_144_6Test {

    private static Method formatMethodWithMillisAndTz;

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        formatMethodWithMillisAndTz = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethodWithMillisAndTz.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillisTrue_returnsStringWithMillis() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L); // Epoch
        String result = (String) formatMethodWithMillisAndTz.invoke(null, date, true, TimeZone.getTimeZone("UTC"));
        // Format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillisFalse_returnsStringWithoutMillis() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L); // Epoch
        String result = (String) formatMethodWithMillisAndTz.invoke(null, date, false, TimeZone.getTimeZone("UTC"));
        // Format: yyyy-MM-dd'T'HH:mm:ss'Z'
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withNonUtcTimeZone_returnsCorrectOffset() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L); // Epoch
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String result = (String) formatMethodWithMillisAndTz.invoke(null, date, false, tz);
        // Format: yyyy-MM-dd'T'HH:mm:ss+02:00
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+02:00"));
    }

    @Test
    @Timeout(8000)
    void testFormat_publicFormatDateMillis_usesDefaultUTC() {
        Date date = new Date(0L);
        String result = ISO8601Utils.format(date, true);
        assertTrue(result.endsWith("Z"));
        assertTrue(result.contains("."));
    }

    @Test
    @Timeout(8000)
    void testFormat_publicFormatDate_noMillis_usesDefaultUTC() {
        Date date = new Date(0L);
        String result = ISO8601Utils.format(date);
        assertTrue(result.endsWith("Z"));
        assertFalse(result.contains("."));
    }
}