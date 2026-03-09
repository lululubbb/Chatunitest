package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

class ISO8601Utils_143_3Test {

    @Test
    @Timeout(8000)
    void testFormat_date_notNull_defaultMillisAndUTC() throws Exception {
        Date date = new Date(0L); // Epoch
        String formatted = ISO8601Utils.format(date);
        assertNotNull(formatted);
        // Format should end with 'Z' for UTC timezone
        assertTrue(formatted.endsWith("Z"));
        // Length should correspond to no milliseconds
        assertFalse(formatted.contains("."));
    }

    @Test
    @Timeout(8000)
    void testFormat_date_withMillis_andCustomTimeZone() throws Exception {
        Date date = new Date(0L); // Epoch
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String formatted = ISO8601Utils.format(date, true, tz);
        assertNotNull(formatted);
        // Should contain milliseconds
        assertTrue(formatted.contains("."));
        // Should end with +02:00 (or equivalent)
        assertTrue(formatted.endsWith("+02:00"));
    }

    @Test
    @Timeout(8000)
    void testFormat_date_withoutMillis_andCustomTimeZone() throws Exception {
        Date date = new Date(0L); // Epoch
        TimeZone tz = TimeZone.getTimeZone("GMT-05:00");
        String formatted = ISO8601Utils.format(date, false, tz);
        assertNotNull(formatted);
        // Should not contain milliseconds
        assertFalse(formatted.contains("."));
        // Should end with -05:00 (or equivalent)
        assertTrue(formatted.endsWith("-05:00"));
    }

    @Test
    @Timeout(8000)
    void testFormat_nullDate_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            ISO8601Utils.format(null);
        });
    }

    @Test
    @Timeout(8000)
    void testPrivateFormatMethodViaReflection() throws Exception {
        Date date = new Date(0L);
        Method formatMethod = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);

        String resultMillis = (String) formatMethod.invoke(null, date, true, TimeZone.getTimeZone("UTC"));
        String resultNoMillis = (String) formatMethod.invoke(null, date, false, TimeZone.getTimeZone("UTC"));

        assertNotNull(resultMillis);
        assertNotNull(resultNoMillis);
        assertTrue(resultMillis.contains("."));
        assertFalse(resultNoMillis.contains("."));
        assertTrue(resultMillis.endsWith("Z"));
        assertTrue(resultNoMillis.endsWith("Z"));
    }
}