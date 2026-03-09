package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601Utils_144_3Test {

    @Test
    @Timeout(8000)
    public void testFormat_withMillisTrue() {
        Date date = new Date(0L); // Epoch start
        String formatted = ISO8601Utils.format(date, true);
        assertNotNull(formatted);
        assertTrue(formatted.contains(".000Z"));
    }

    @Test
    @Timeout(8000)
    public void testFormat_withMillisFalse() {
        Date date = new Date(0L); // Epoch start
        String formatted = ISO8601Utils.format(date, false);
        assertNotNull(formatted);
        assertTrue(formatted.endsWith("Z"));
        assertFalse(formatted.contains("."));
    }

    @Test
    @Timeout(8000)
    public void testFormat_withDefaultMillis() {
        Date date = new Date(0L);
        String formatted = ISO8601Utils.format(date);
        assertNotNull(formatted);
        assertTrue(formatted.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    public void testFormat_withMillisAndTimeZone() {
        Date date = new Date(0L);
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String formatted = ISO8601Utils.format(date, true, tz);
        assertNotNull(formatted);
        assertTrue(formatted.contains("+02:00"));
        assertTrue(formatted.contains(".000"));
    }

    @Test
    @Timeout(8000)
    public void testFormat_withMillisFalseAndTimeZone() {
        Date date = new Date(0L);
        TimeZone tz = TimeZone.getTimeZone("GMT-05:00");
        String formatted = ISO8601Utils.format(date, false, tz);
        assertNotNull(formatted);
        assertTrue(formatted.contains("-05:00"));
        assertFalse(formatted.contains("."));
    }

    @Test
    @Timeout(8000)
    public void testPrivateCheckOffset_trueAndFalse() throws Exception {
        Method checkOffset = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffset.setAccessible(true);

        // Expected char matches
        boolean resultTrue = (boolean) checkOffset.invoke(null, "2023-04-01", 4, '-');
        assertTrue(resultTrue);

        // Expected char does not match
        boolean resultFalse = (boolean) checkOffset.invoke(null, "2023/04/01", 4, '-');
        assertFalse(resultFalse);

        // Offset out of bounds
        boolean resultFalse2 = (boolean) checkOffset.invoke(null, "2023", 10, '-');
        assertFalse(resultFalse2);
    }

    @Test
    @Timeout(8000)
    public void testPrivateParseInt_validAndInvalid() throws Exception {
        Method parseInt = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseInt.setAccessible(true);

        int val = (int) parseInt.invoke(null, "1234567890", 2, 5);
        assertEquals(345, val);

        // Invalid number should throw NumberFormatException wrapped in InvocationTargetException
        assertThrows(Exception.class, () -> parseInt.invoke(null, "12a4567890", 2, 5));
    }

    @Test
    @Timeout(8000)
    public void testPrivatePadInt() throws Exception {
        Method padInt = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padInt.setAccessible(true);

        StringBuilder sb = new StringBuilder();
        padInt.invoke(null, sb, 5, 3);
        assertEquals("005", sb.toString());

        sb = new StringBuilder();
        padInt.invoke(null, sb, 1234, 2);
        assertEquals("1234", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrivateIndexOfNonDigit() throws Exception {
        Method indexOfNonDigit = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigit.setAccessible(true);

        int idx = (int) indexOfNonDigit.invoke(null, "12345abc", 0);
        assertEquals(5, idx);

        idx = (int) indexOfNonDigit.invoke(null, "1234567890", 0);
        assertEquals(10, idx);

        idx = (int) indexOfNonDigit.invoke(null, "abc123", 0);
        assertEquals(0, idx);
    }
}