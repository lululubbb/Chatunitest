package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_146_1Test {

    private ParsePosition pos;

    @BeforeEach
    public void setUp() {
        pos = new ParsePosition(0);
    }

    @Test
    @Timeout(8000)
    public void testParseDateOnly() throws Exception {
        String dateStr = "20230415";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateWithDashes() throws Exception {
        String dateStr = "2023-04-15";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeUTC() throws Exception {
        String dateStr = "2023-04-15T12:34:56Z";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithMillisUTC() throws Exception {
        String dateStr = "2023-04-15T12:34:56.789Z";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithPositiveOffset() throws Exception {
        String dateStr = "2023-04-15T12:34:56+02:00";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithNegativeOffset() throws Exception {
        String dateStr = "2023-04-15T12:34:56-0500";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithLeapSeconds() throws Exception {
        String dateStr = "2023-04-15T12:34:60Z";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithFractionalSecondsOneDigit() throws Exception {
        String dateStr = "2023-04-15T12:34:56.7Z";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithFractionalSecondsTwoDigits() throws Exception {
        String dateStr = "2023-04-15T12:34:56.78Z";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseInvalidTimezoneIndicator() {
        String dateStr = "2023-04-15T12:34:56X";
        ParsePosition pos = new ParsePosition(0);
        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class, () -> {
            ISO8601Utils.parse(dateStr, pos);
        });
        assertTrue(ex.getMessage().contains("Invalid time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseNoTimezoneIndicator() {
        String dateStr = "2023-04-15T12:34:56";
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> {
            ISO8601Utils.parse(dateStr, pos);
        });
        assertTrue(ex.getMessage().contains("No time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseNullDate() {
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> {
            ISO8601Utils.parse(null, pos);
        });
        assertTrue(ex.getMessage().contains("Failed to parse date [null]"));
    }

    @Test
    @Timeout(8000)
    public void testParseWithInvalidDateFormat() {
        String dateStr = "abcd";
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> {
            ISO8601Utils.parse(dateStr, pos);
        });
        assertTrue(ex.getMessage().contains("Failed to parse date"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateCheckOffset() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, "abc-def", 3, '-');
        assertTrue(result);
        result = (boolean) method.invoke(null, "abcdef", 3, '-');
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testPrivateParseInt() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        method.setAccessible(true);
        int val = (int) method.invoke(null, "1234567890", 2, 5);
        assertEquals(345, val);
    }

    @Test
    @Timeout(8000)
    public void testPrivateIndexOfNonDigit() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        method.setAccessible(true);
        int idx = (int) method.invoke(null, "12345abc678", 0);
        assertEquals(5, idx);
        idx = (int) method.invoke(null, "123456789", 0);
        assertEquals(9, idx);
    }
}