package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_146_4Test {

    private Method parseIntMethod;
    private Method checkOffsetMethod;
    private Method indexOfNonDigitMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);

        checkOffsetMethod = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffsetMethod.setAccessible(true);

        indexOfNonDigitMethod = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigitMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testParseDateOnly() throws ParseException {
        String input = "2023-04-25";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeUTCWithMillis() throws ParseException {
        String input = "2023-04-25T14:30:45.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeUTCWithoutMillis() throws ParseException {
        String input = "2023-04-25T14:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithPositiveTimezone() throws ParseException {
        String input = "2023-04-25T14:30:45+02:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithNegativeTimezone() throws ParseException {
        String input = "2023-04-25T14:30:45-0530";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithLeapSecond() throws ParseException {
        String input = "2023-04-25T14:30:61Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithFractionalSecondsOneDigit() throws ParseException {
        String input = "2023-04-25T14:30:45.1Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeWithFractionalSecondsTwoDigits() throws ParseException {
        String input = "2023-04-25T14:30:45.12Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeMissingTimeZone() {
        String input = "2023-04-25T14:30:45";
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(input, pos));
        assertTrue(ex.getMessage().contains("No time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseDateTimeInvalidTimeZoneIndicator() {
        String input = "2023-04-25T14:30:45X";
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(input, pos));
        assertTrue(ex.getMessage().contains("Invalid time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void testParseDateWithInvalidNumberFormat() {
        String input = "2023-AB-25";
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(input, pos));
        assertTrue(ex.getMessage().contains("Failed to parse date"));
    }

    @Test
    @Timeout(8000)
    public void testParseDateWithInvalidIndexOutOfBounds() {
        String input = "2023-04";
        ParsePosition pos = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(input, pos));
        assertTrue(ex.getMessage().contains("Failed to parse date"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateParseInt() throws InvocationTargetException, IllegalAccessException {
        String input = "1234567890";
        int result = (int) parseIntMethod.invoke(null, input, 2, 6);
        assertEquals(3456, result);
    }

    @Test
    @Timeout(8000)
    public void testPrivateCheckOffset() throws InvocationTargetException, IllegalAccessException {
        String input = "2023-04-25";
        boolean result = (boolean) checkOffsetMethod.invoke(null, input, 4, '-');
        assertTrue(result);
        boolean resultFalse = (boolean) checkOffsetMethod.invoke(null, input, 5, '-');
        assertFalse(resultFalse);
    }

    @Test
    @Timeout(8000)
    public void testPrivateIndexOfNonDigit() throws InvocationTargetException, IllegalAccessException {
        String input = "1234abc567";
        int index = (int) indexOfNonDigitMethod.invoke(null, input, 0);
        assertEquals(4, index);
        int indexFrom5 = (int) indexOfNonDigitMethod.invoke(null, input, 5);
        assertEquals(6, indexFrom5);
        int indexNone = (int) indexOfNonDigitMethod.invoke(null, "1234567890", 0);
        assertEquals(-1, indexNone);
    }
}