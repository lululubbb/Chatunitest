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

public class ISO8601Utils_146_2Test {

    private ParsePosition pos;

    @BeforeEach
    public void setUp() {
        pos = new ParsePosition(0);
    }

    @Test
    @Timeout(8000)
    public void testParseDateOnly() throws Exception {
        String dateStr = "20230425";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateWithHyphens() throws Exception {
        String dateStr = "2023-04-25";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithZ() throws Exception {
        String dateStr = "2023-04-25T12:34:56Z";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithMillisAndZ() throws Exception {
        String dateStr = "2023-04-25T12:34:56.789Z";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithPositiveTimezone() throws Exception {
        String dateStr = "2023-04-25T12:34:56+02:00";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithNegativeTimezone() throws Exception {
        String dateStr = "2023-04-25T12:34:56-0500";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithLeapSeconds() throws Exception {
        String dateStr = "2023-04-25T12:34:60Z";
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithMillisLessThan3Digits() throws Exception {
        String dateStr1 = "2023-04-25T12:34:56.7Z";
        pos.setIndex(0);
        Date date1 = ISO8601Utils.parse(dateStr1, pos);
        assertNotNull(date1);
        assertEquals(dateStr1.length(), pos.getIndex());

        pos.setIndex(0);
        String dateStr2 = "2023-04-25T12:34:56.78Z";
        Date date2 = ISO8601Utils.parse(dateStr2, pos);
        assertNotNull(date2);
        assertEquals(dateStr2.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseThrowsOnInvalidTimezoneIndicator() {
        String dateStr = "2023-04-25T12:34:56X";
        ParsePosition pos = new ParsePosition(0);
        IndexOutOfBoundsException thrown = assertThrows(IndexOutOfBoundsException.class, () -> {
            ISO8601Utils.parse(dateStr, pos);
        });
        assertTrue(thrown.getMessage().contains("Invalid time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseThrowsOnNoTimezoneIndicator() {
        String dateStr = "2023-04-25T12:34:56";
        ParsePosition pos = new ParsePosition(0);
        ParseException thrown = assertThrows(ParseException.class, () -> {
            ISO8601Utils.parse(dateStr, pos);
        });
        assertTrue(thrown.getMessage().contains("No time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseThrowsOnInvalidDate() {
        String dateStr = "abcd-ef-ghT12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        ParseException thrown = assertThrows(ParseException.class, () -> {
            ISO8601Utils.parse(dateStr, pos);
        });
        assertTrue(thrown.getMessage().contains("Failed to parse date"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateCheckOffset() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(null, "2023-04-25", 4, '-');
        assertTrue(result);
        boolean resultFalse = (boolean) method.invoke(null, "2023-04-25", 4, 'X');
        assertFalse(resultFalse);
    }

    @Test
    @Timeout(8000)
    public void testPrivateParseInt() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        method.setAccessible(true);
        int val = (int) method.invoke(null, "20230425", 0, 4);
        assertEquals(2023, val);
        int val2 = (int) method.invoke(null, "20230425", 4, 6);
        assertEquals(4, val2);
    }

    @Test
    @Timeout(8000)
    public void testPrivateIndexOfNonDigit() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        method.setAccessible(true);
        int idx = (int) method.invoke(null, "1234abc", 0);
        assertEquals(4, idx);
        int idx2 = (int) method.invoke(null, "123456", 0);
        assertEquals(6, idx2);
    }

    @Test
    @Timeout(8000)
    public void testPrivatePadInt() throws Exception {
        Method method = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        method.setAccessible(true);
        StringBuilder sb = new StringBuilder();
        method.invoke(null, sb, 7, 3);
        assertEquals("007", sb.toString());
    }
}