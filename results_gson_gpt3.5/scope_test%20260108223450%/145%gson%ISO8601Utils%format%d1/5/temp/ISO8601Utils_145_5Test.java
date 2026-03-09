package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParsePosition;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_145_5Test {

    private static Method padIntMethod;

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        padIntMethod = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillis_UTC() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(tz, Locale.US);
        cal.set(2024, Calendar.JUNE, 10, 15, 30, 45);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        assertEquals("2024-06-10T15:30:45.123Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_withoutMillis_UTC() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(tz, Locale.US);
        cal.set(2024, Calendar.JUNE, 10, 15, 30, 45);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, false, tz);
        assertEquals("2024-06-10T15:30:45Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillis_PositiveOffset() {
        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar cal = new GregorianCalendar(tz, Locale.US);
        cal.set(2024, Calendar.JUNE, 10, 15, 30, 45);
        cal.set(Calendar.MILLISECOND, 456);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        // offset +05:30 expected
        assertTrue(formatted.startsWith("2024-06-10T15:30:45.456+05:30"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withoutMillis_NegativeOffset() {
        TimeZone tz = TimeZone.getTimeZone("GMT-03:00");
        Calendar cal = new GregorianCalendar(tz, Locale.US);
        cal.set(2024, Calendar.JUNE, 10, 15, 30, 45);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, false, tz);
        // offset -03:00 expected
        assertTrue(formatted.startsWith("2024-06-10T15:30:45-03:00"));
    }

    @Test
    @Timeout(8000)
    void testFormat_midnightAndSingleDigitFields() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(tz, Locale.US);
        cal.set(2024, Calendar.JANUARY, 5, 3, 4, 5);
        cal.set(Calendar.MILLISECOND, 7);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        assertEquals("2024-01-05T03:04:05.007Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testPadInt_privateMethod() throws InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        // padInt(sb, 7, 3) -> "007"
        padIntMethod.invoke(null, sb, 7, 3);
        assertEquals("007", sb.toString());

        sb.setLength(0);
        // padInt(sb, 123, 2) -> "123" (no truncation, pads only if shorter)
        padIntMethod.invoke(null, sb, 123, 2);
        assertEquals("123", sb.toString());

        sb.setLength(0);
        // padInt(sb, 0, 4) -> "0000"
        padIntMethod.invoke(null, sb, 0, 4);
        assertEquals("0000", sb.toString());
    }
}