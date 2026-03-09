package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_146_6Test {

    private ParsePosition pos;

    @BeforeEach
    public void setUp() {
        pos = new ParsePosition(0);
    }

    @Test
    @Timeout(8000)
    public void testParseDateOnly() throws Exception {
        String dateStr = "2023-04-15";
        Date parsed = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(parsed);
        assertEquals(dateStr.length(), pos.getIndex());

        Calendar cal = Calendar.getInstance();
        cal.setTime(parsed);
        assertEquals(2023, cal.get(Calendar.YEAR));
        assertEquals(3, cal.get(Calendar.MONTH)); // zero-based
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(0, cal.get(Calendar.MILLISECOND));
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeUTC() throws Exception {
        String dateStr = "2023-04-15T12:34:56Z";
        Date parsed = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(parsed);
        assertEquals(dateStr.length(), pos.getIndex());

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(parsed);
        assertEquals(2023, cal.get(Calendar.YEAR));
        assertEquals(3, cal.get(Calendar.MONTH));
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(34, cal.get(Calendar.MINUTE));
        assertEquals(56, cal.get(Calendar.SECOND));
        assertEquals(0, cal.get(Calendar.MILLISECOND));
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithMillisAndTimezone() throws Exception {
        String dateStr = "2023-04-15T12:34:56.789+0200";
        Date parsed = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(parsed);
        assertEquals(dateStr.length(), pos.getIndex());

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+0200"));
        cal.setTime(parsed);
        assertEquals(2023, cal.get(Calendar.YEAR));
        assertEquals(3, cal.get(Calendar.MONTH));
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(34, cal.get(Calendar.MINUTE));
        assertEquals(56, cal.get(Calendar.SECOND));
        assertEquals(789, cal.get(Calendar.MILLISECOND));
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithMillisAndTimezoneColon() throws Exception {
        String dateStr = "2023-04-15T12:34:56.7+02:00";
        Date parsed = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(parsed);
        assertEquals(dateStr.length(), pos.getIndex());

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+02:00"));
        cal.setTime(parsed);
        assertEquals(2023, cal.get(Calendar.YEAR));
        assertEquals(3, cal.get(Calendar.MONTH));
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(34, cal.get(Calendar.MINUTE));
        assertEquals(56, cal.get(Calendar.SECOND));
        assertEquals(700, cal.get(Calendar.MILLISECOND));
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithLeapSecond() throws Exception {
        String dateStr = "2023-04-15T12:34:61Z";
        Date parsed = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(parsed);
        assertEquals(dateStr.length(), pos.getIndex());

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(parsed);
        assertEquals(59, cal.get(Calendar.SECOND)); // leap second truncated to 59
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeMissingTimezone() {
        String dateStr = "2023-04-15T12:34:56";
        ParsePosition posLocal = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateStr, posLocal));
        assertTrue(ex.getMessage().contains("No time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseInvalidDate() {
        String dateStr = "2023-04-15T12:34:56X";
        ParsePosition posLocal = new ParsePosition(0);
        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class, () -> ISO8601Utils.parse(dateStr, posLocal));
        assertTrue(ex.getMessage().contains("Invalid time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseNullDate() {
        ParsePosition posLocal = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(null, posLocal));
        assertTrue(ex.getMessage().contains("Failed to parse date [null]"));
    }

    @Test
    @Timeout(8000)
    public void testParseShortDate() {
        String dateStr = "202";
        ParsePosition posLocal = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateStr, posLocal));
        assertTrue(ex.getMessage().contains("Failed to parse date"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateCheckOffsetMethod() throws Exception {
        Method checkOffset = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffset.setAccessible(true);

        assertTrue((Boolean) checkOffset.invoke(null, "2023-04-15", 4, '-'));
        assertFalse((Boolean) checkOffset.invoke(null, "2023/04/15", 4, '-'));
    }

    @Test
    @Timeout(8000)
    public void testPrivateParseIntMethod() throws Exception {
        Method parseInt = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseInt.setAccessible(true);

        int val = (int) parseInt.invoke(null, "1234567890", 2, 5);
        assertEquals(345, val);
    }

    @Test
    @Timeout(8000)
    public void testPrivateIndexOfNonDigitMethod() throws Exception {
        Method indexOfNonDigit = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigit.setAccessible(true);

        int idx = (int) indexOfNonDigit.invoke(null, "123abc456", 0);
        assertEquals(3, idx);

        idx = (int) indexOfNonDigit.invoke(null, "123456", 0);
        assertEquals(6, idx);
    }
}