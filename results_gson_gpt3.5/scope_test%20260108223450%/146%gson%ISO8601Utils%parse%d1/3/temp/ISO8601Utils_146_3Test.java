package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_146_3Test {

    private Method parseIntMethod;
    private Method checkOffsetMethod;
    private Method indexOfNonDigitMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Access private static methods via reflection
        parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);

        checkOffsetMethod = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffsetMethod.setAccessible(true);

        indexOfNonDigitMethod = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigitMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testParseDateOnly() throws Exception {
        String dateStr = "20230405";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateWithDashes() throws Exception {
        String dateStr = "2023-04-05";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeUTC() throws Exception {
        String dateStr = "2023-04-05T12:34:56Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithMillisUTC() throws Exception {
        String dateStr = "2023-04-05T12:34:56.789Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithPositiveTimezone() throws Exception {
        String dateStr = "2023-04-05T12:34:56+02:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithNegativeTimezone() throws Exception {
        String dateStr = "2023-04-05T12:34:56-0230";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithLeapSecond() throws Exception {
        String dateStr = "2023-04-05T12:34:61Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(dateStr, pos);
        assertNotNull(date);
        assertEquals(dateStr.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithMillisLessDigits() throws Exception {
        String dateStr1 = "2023-04-05T12:34:56.7Z";
        String dateStr2 = "2023-04-05T12:34:56.78Z";

        ParsePosition pos1 = new ParsePosition(0);
        Date date1 = ISO8601Utils.parse(dateStr1, pos1);
        assertNotNull(date1);
        assertEquals(dateStr1.length(), pos1.getIndex());

        ParsePosition pos2 = new ParsePosition(0);
        Date date2 = ISO8601Utils.parse(dateStr2, pos2);
        assertNotNull(date2);
        assertEquals(dateStr2.length(), pos2.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseThrowsOnInvalidTimezoneIndicator() {
        String dateStr = "2023-04-05T12:34:56X";
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateStr, pos));
        assertTrue(ex.getMessage().contains("Invalid time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseThrowsOnNoTimeZoneIndicator() {
        String dateStr = "2023-04-05T12:34:56";
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateStr, pos));
        assertTrue(ex.getMessage().contains("No time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseThrowsOnInvalidDate() {
        String dateStr = "abcd";
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateStr, pos));
        assertTrue(ex.getMessage().contains("Failed to parse date"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateParseInt() throws Exception {
        int val = (int) parseIntMethod.invoke(null, "1234567890", 2, 5);
        assertEquals(345, val);
    }

    @Test
    @Timeout(8000)
    public void testPrivateCheckOffset() throws Exception {
        boolean resultTrue = (boolean) checkOffsetMethod.invoke(null, "abc-def", 3, '-');
        boolean resultFalse = (boolean) checkOffsetMethod.invoke(null, "abcdef", 3, '-');
        assertTrue(resultTrue);
        assertFalse(resultFalse);
    }

    @Test
    @Timeout(8000)
    public void testPrivateIndexOfNonDigit() throws Exception {
        int idx = (int) indexOfNonDigitMethod.invoke(null, "123abc456", 0);
        assertEquals(3, idx);
        int idx2 = (int) indexOfNonDigitMethod.invoke(null, "123456", 0);
        assertEquals(6, idx2);
    }
}