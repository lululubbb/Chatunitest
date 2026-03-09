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

import org.junit.jupiter.api.Test;

class ISO8601Utils_145_2Test {

    @Test
    @Timeout(8000)
    void testFormat_withMillis_UTC() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2023, Calendar.MARCH, 15, 10, 20, 30);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        assertEquals("2023-03-15T10:20:30.123Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_withoutMillis_UTC() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2023, Calendar.MARCH, 15, 10, 20, 30);
        cal.set(Calendar.MILLISECOND, 456);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, false, tz);
        assertEquals("2023-03-15T10:20:30Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillis_nonUTC_PositiveOffset() {
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2023, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 789);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        // Offset +02:30
        assertTrue(formatted.startsWith("2023-12-31T23:59:59.789+02:30"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withoutMillis_nonUTC_NegativeOffset() {
        TimeZone tz = TimeZone.getTimeZone("GMT-05:00");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2023, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, false, tz);
        assertTrue(formatted.startsWith("2023-01-01T00:00:00-05:00"));
    }

    @Test
    @Timeout(8000)
    void testFormat_leapDay_withMillis() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2020, Calendar.FEBRUARY, 29, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 1);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        assertEquals("2020-02-29T12:00:00.001Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_leapSecond_like() {
        // Since Java Calendar does not support leap seconds, test 23:59:60 as 23:59:59 with 999 millis
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2016, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        assertEquals("2016-12-31T23:59:59.999Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_reflection_padInt() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method padIntMethod = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);

        StringBuilder sb = new StringBuilder();
        // padInt with value=7 length=3 -> "007"
        padIntMethod.invoke(null, sb, 7, 3);
        assertEquals("007", sb.toString());

        sb.setLength(0);
        // padInt with value=123 length=3 -> "123"
        padIntMethod.invoke(null, sb, 123, 3);
        assertEquals("123", sb.toString());

        sb.setLength(0);
        // padInt with value=1234 length=3 -> "1234" (no truncation)
        padIntMethod.invoke(null, sb, 1234, 3);
        assertEquals("1234", sb.toString());
    }
}