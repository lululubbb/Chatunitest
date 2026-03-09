package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParsePosition;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ISO8601Utils_145_4Test {

    private static Method padIntMethod;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        padIntMethod = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillis_UTC() {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(utc);
        cal.clear();
        cal.set(2023, Calendar.MARCH, 15, 10, 20, 30);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, utc);
        assertEquals("2023-03-15T10:20:30.123Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_withoutMillis_UTC() {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(utc);
        cal.clear();
        cal.set(2023, Calendar.MARCH, 15, 10, 20, 30);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, false, utc);
        assertEquals("2023-03-15T10:20:30Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillis_nonZeroOffsetPositive() {
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        Calendar cal = Calendar.getInstance(tz);
        cal.clear();
        cal.set(2023, Calendar.JANUARY, 1, 5, 6, 7);
        cal.set(Calendar.MILLISECOND, 5);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        // Offset +02:30
        assertTrue(formatted.startsWith("2023-01-01T05:06:07.005+02:30"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withoutMillis_nonZeroOffsetNegative() {
        TimeZone tz = TimeZone.getTimeZone("GMT-05:45");
        Calendar cal = Calendar.getInstance(tz);
        cal.clear();
        cal.set(2023, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, false, tz);
        // Offset -05:45
        assertTrue(formatted.startsWith("2023-12-31T23:59:59-05:45"));
    }

    @Test
    @Timeout(8000)
    void testFormat_epochZero_UTC() {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date date = new Date(0L);

        String formatted = ISO8601Utils.format(date, true, utc);
        assertEquals("1970-01-01T00:00:00.000Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testPadInt_privateMethod() throws IllegalAccessException, InvocationTargetException {
        StringBuilder sb = new StringBuilder();
        // pad 5 to length 3 -> "005"
        padIntMethod.invoke(null, sb, 5, 3);
        assertEquals("005", sb.toString());

        sb.setLength(0);
        // pad 123 to length 3 -> "123"
        padIntMethod.invoke(null, sb, 123, 3);
        assertEquals("123", sb.toString());

        sb.setLength(0);
        // pad 1234 to length 3 -> "1234" (no truncation)
        padIntMethod.invoke(null, sb, 1234, 3);
        assertEquals("1234", sb.toString());

        sb.setLength(0);
        // pad 0 to length 2 -> "00"
        padIntMethod.invoke(null, sb, 0, 2);
        assertEquals("00", sb.toString());
    }
}