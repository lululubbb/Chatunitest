package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601Utils_144_4Test {

    @Test
    @Timeout(8000)
    public void testFormat_dateAndMillis_true() {
        Date date = new Date(0L); // Epoch
        String formatted = ISO8601Utils.format(date, true);
        assertNotNull(formatted);
        // Expected format: 1970-01-01T00:00:00.000Z
        assertTrue(formatted.startsWith("1970-01-01T00:00:00."));
        assertTrue(formatted.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    public void testFormat_dateAndMillis_false() {
        Date date = new Date(0L); // Epoch
        String formatted = ISO8601Utils.format(date, false);
        assertNotNull(formatted);
        // Expected format: 1970-01-01T00:00:00Z
        assertEquals("1970-01-01T00:00:00Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormat_dateOnly() {
        Date date = new Date(0L);
        String formatted = ISO8601Utils.format(date);
        assertNotNull(formatted);
        assertEquals("1970-01-01T00:00:00Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormat_dateMillisAndCustomTimeZone() {
        Date date = new Date(0L);
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String formatted = ISO8601Utils.format(date, true, tz);
        assertNotNull(formatted);
        assertTrue(formatted.contains("+02:00") || formatted.contains("+0200"));
    }

    @Test
    @Timeout(8000)
    public void testFormat_dateNoMillisCustomTimeZone() {
        Date date = new Date(0L);
        TimeZone tz = TimeZone.getTimeZone("GMT-05:00");
        String formatted = ISO8601Utils.format(date, false, tz);
        assertNotNull(formatted);
        assertTrue(formatted.contains("-05:00") || formatted.contains("-0500"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateCheckOffset() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        method.setAccessible(true);

        // offset in range and char matches
        boolean result1 = (boolean) method.invoke(null, "2023-06-07T12:00:00Z", 4, '-');
        assertTrue(result1);

        // offset in range but char does not match
        boolean result2 = (boolean) method.invoke(null, "2023-06-07T12:00:00Z", 4, 'T');
        assertFalse(result2);

        // offset out of range
        boolean result3 = (boolean) method.invoke(null, "2023-06-07T12:00:00Z", 100, 'Z');
        assertFalse(result3);
    }

    @Test
    @Timeout(8000)
    public void testPrivateParseInt() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        method.setAccessible(true);

        int val = (int) method.invoke(null, "20230607", 0, 4);
        assertEquals(2023, val);

        int val2 = (int) method.invoke(null, "20230607", 7, 8);
        assertEquals(7, val2);

        int val3 = (int) method.invoke(null, "00001234", 4, 8);
        assertEquals(1234, val3);
    }

    @Test
    @Timeout(8000)
    public void testPrivatePadInt() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        method.setAccessible(true);

        StringBuilder sb = new StringBuilder();
        method.invoke(null, sb, 5, 3);
        assertEquals("005", sb.toString());

        sb = new StringBuilder();
        method.invoke(null, sb, 123, 2);
        assertEquals("123", sb.toString());

        sb = new StringBuilder();
        method.invoke(null, sb, 0, 4);
        assertEquals("0000", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrivateIndexOfNonDigit() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        method.setAccessible(true);

        int idx1 = (int) method.invoke(null, "20230607abc", 0);
        assertEquals(8, idx1);

        int idx2 = (int) method.invoke(null, "123456", 0);
        assertEquals(-1, idx2);

        int idx3 = (int) method.invoke(null, "abc123", 0);
        assertEquals(0, idx3);

        int idx4 = (int) method.invoke(null, "123abc456", 3);
        assertEquals(3, idx4);
    }

    @Test
    @Timeout(8000)
    public void testParse_withValidDate() throws ParseException {
        String dateStr = "2023-06-07T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(parsedDate);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParse_withInvalidDate() throws ParseException {
        String dateStr = "invalid-date";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = null;
        try {
            parsedDate = ISO8601Utils.parse(dateStr, pos);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
        assertNull(parsedDate);
        // pos index should remain 0
        assertEquals(0, pos.getIndex());
    }
}