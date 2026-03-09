package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ISO8601Utils_143_4Test {

    private Date date;
    private TimeZone tz;

    @BeforeEach
    void setUp() {
        date = new Date(0L); // Epoch time
        tz = TimeZone.getTimeZone("GMT+02:00");
    }

    @Test
    @Timeout(8000)
    void testFormat_dateOnly() {
        String result = ISO8601Utils.format(date);
        assertNotNull(result);
        assertTrue(result.contains("1970-01-01"));
        assertTrue(result.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillisTrue() {
        String result = ISO8601Utils.format(date, true);
        assertNotNull(result);
        assertTrue(result.contains("1970-01-01"));
        assertTrue(result.contains(".000"));
        assertTrue(result.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillisFalse() {
        String result = ISO8601Utils.format(date, false);
        assertNotNull(result);
        assertTrue(result.contains("1970-01-01"));
        assertFalse(result.contains(".000"));
        assertTrue(result.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withMillisAndTimeZone() {
        String result = ISO8601Utils.format(date, true, tz);
        assertNotNull(result);
        // The date is 1970-01-01T00:00:00Z, with GMT+02:00 timezone,
        // local time is 1970-01-01T02:00:00+02:00, so date part remains "1970-01-01"
        // Adjust expectation accordingly
        assertTrue(result.contains("1970-01-01"));
        assertTrue(result.contains(".000"));
        assertTrue(result.endsWith("+02:00"));
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullDate() {
        assertThrows(NullPointerException.class, () -> ISO8601Utils.format(null));
        assertThrows(NullPointerException.class, () -> ISO8601Utils.format(null, true));
        assertThrows(NullPointerException.class, () -> ISO8601Utils.format(null, true, tz));
    }

    @Test
    @Timeout(8000)
    void testPrivateCheckOffset_method() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, "abc-def", 3, '-'));
        assertFalse((Boolean) method.invoke(null, "abcdef", 3, '-'));
        assertFalse((Boolean) method.invoke(null, "abc-def", 2, '-'));
    }

    @Test
    @Timeout(8000)
    void testPrivateParseInt_method() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        method.setAccessible(true);

        assertEquals(123, method.invoke(null, "abc123def", 3, 6));
        assertEquals(0, method.invoke(null, "0000", 0, 4));

        // Test invalid number: expect NumberFormatException wrapped in InvocationTargetException
        Exception ex = assertThrows(InvocationTargetException.class, () -> method.invoke(null, "abc12x", 3, 6));
        assertTrue(ex.getCause() instanceof NumberFormatException);
    }

    @Test
    @Timeout(8000)
    void testPrivatePadInt_method() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        method.setAccessible(true);

        StringBuilder sb = new StringBuilder();
        method.invoke(null, sb, 5, 3);
        assertEquals("005", sb.toString());

        sb.setLength(0);
        method.invoke(null, sb, 123, 2);
        assertEquals("123", sb.toString()); // No truncation
    }

    @Test
    @Timeout(8000)
    void testPrivateIndexOfNonDigit_method() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        method.setAccessible(true);

        assertEquals(3, method.invoke(null, "123a45", 0));
        assertEquals(6, method.invoke(null, "123456", 0));
        assertEquals(5, method.invoke(null, "12a34", 3));
    }

    @Test
    @Timeout(8000)
    void testParse_validDate() throws Exception {
        String dateStr = "1970-01-01T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsed = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(parsed);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    void testParse_invalidDate() throws Exception {
        String dateStr = "invalid-date";
        ParsePosition pos = new ParsePosition(0);
        Date parsed = null;
        try {
            parsed = ISO8601Utils.parse(dateStr, pos);
            fail("Expected ParseException was not thrown");
        } catch (ParseException e) {
            // Expected exception, test passes
        }
        assertNull(parsed);
        // ParsePosition errorIndex may be set to -1 or 0 depending on implementation, so no assert here
    }
}