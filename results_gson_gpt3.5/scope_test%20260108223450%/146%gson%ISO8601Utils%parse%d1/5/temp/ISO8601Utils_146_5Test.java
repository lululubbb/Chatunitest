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

public class ISO8601Utils_146_5Test {

    private ParsePosition pos;

    @BeforeEach
    public void setUp() {
        pos = new ParsePosition(0);
    }

    @Test
    @Timeout(8000)
    public void parse_dateOnly() throws Exception {
        String input = "2023-04-15";
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        assertEquals(2023, cal.get(Calendar.YEAR));
        assertEquals(3, cal.get(Calendar.MONTH)); // zero-based month
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(0, cal.get(Calendar.MILLISECOND));
    }

    @Test
    @Timeout(8000)
    public void parse_dateTimeUTC() throws Exception {
        String input = "2023-04-15T14:30:20Z";
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        assertEquals(2023, cal.get(Calendar.YEAR));
        assertEquals(3, cal.get(Calendar.MONTH));
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, cal.get(Calendar.MINUTE));
        assertEquals(20, cal.get(Calendar.SECOND));
        assertEquals(0, cal.get(Calendar.MILLISECOND));
    }

    @Test
    @Timeout(8000)
    public void parse_dateTimeWithMillisAndOffset() throws Exception {
        String input = "2023-04-15T14:30:20.123+02:00";
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+02:00"));
        cal.setTime(date);
        assertEquals(2023, cal.get(Calendar.YEAR));
        assertEquals(3, cal.get(Calendar.MONTH));
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, cal.get(Calendar.MINUTE));
        assertEquals(20, cal.get(Calendar.SECOND));
        assertEquals(123, cal.get(Calendar.MILLISECOND));
    }

    @Test
    @Timeout(8000)
    public void parse_dateTimeWithMillisAndNegativeOffset() throws Exception {
        String input = "2023-04-15T14:30:20.1-0230";
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-0230"));
        cal.setTime(date);
        assertEquals(2023, cal.get(Calendar.YEAR));
        assertEquals(3, cal.get(Calendar.MONTH));
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, cal.get(Calendar.MINUTE));
        assertEquals(20, cal.get(Calendar.SECOND));
        assertEquals(100, cal.get(Calendar.MILLISECOND)); // 0.1 sec = 100 ms
    }

    @Test
    @Timeout(8000)
    public void parse_dateTimeWithLeapSecond() throws Exception {
        String input = "2023-04-15T14:30:62Z";
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length(), pos.getIndex());
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        assertEquals(59, cal.get(Calendar.SECOND)); // leap second truncated to 59
    }

    @Test
    @Timeout(8000)
    public void parse_invalidDate_throwsParseException() {
        String input = "2023-04-31"; // April 31 invalid date
        ParsePosition p = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(input, p));
        assertTrue(ex.getMessage().contains(input));
    }

    @Test
    @Timeout(8000)
    public void parse_invalidFormat_throwsParseException() {
        String input = "2023-04-15T14:30:20X";
        ParsePosition p = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(input, p));
        assertTrue(ex.getMessage().contains(input));
    }

    @Test
    @Timeout(8000)
    public void parse_invalidTimeZoneIndicator_throwsIndexOutOfBoundsException() {
        String input = "2023-04-15T14:30:20A";
        ParsePosition p = new ParsePosition(0);
        ParseException ex = assertThrows(ParseException.class, () -> ISO8601Utils.parse(input, p));
        assertTrue(ex.getCause() instanceof IndexOutOfBoundsException);
        assertTrue(ex.getMessage().contains("Invalid time zone indicator"));
    }

    @Test
    @Timeout(8000)
    public void parse_timezoneOffsetWithoutMinutes_appendsMinutes() throws Exception {
        String input = "2023-04-15T14:30:20+02";
        Date date = ISO8601Utils.parse(input, pos);
        assertNotNull(date);
        assertEquals(input.length() + 2, pos.getIndex()); // + "00" appended for minutes
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+0200"));
        cal.setTime(date);
        assertEquals(2023, cal.get(Calendar.YEAR));
    }

    @Test
    @Timeout(8000)
    public void parse_timezoneOffsetEqualsUTC() throws Exception {
        String input1 = "2023-04-15T14:30:20+0000";
        ParsePosition p1 = new ParsePosition(0);
        Date date1 = ISO8601Utils.parse(input1, p1);
        assertNotNull(date1);
        assertEquals(input1.length(), p1.getIndex());

        String input2 = "2023-04-15T14:30:20+00:00";
        ParsePosition p2 = new ParsePosition(0);
        Date date2 = ISO8601Utils.parse(input2, p2);
        assertNotNull(date2);
        assertEquals(input2.length(), p2.getIndex());
    }

    @Test
    @Timeout(8000)
    public void parse_privateCheckOffset_trueAndFalse() throws Exception {
        Method checkOffset = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffset.setAccessible(true);
        assertTrue((Boolean) checkOffset.invoke(null, "2023-04-15", 4, '-'));
        assertFalse((Boolean) checkOffset.invoke(null, "2023-04-15", 4, 'X'));
    }

    @Test
    @Timeout(8000)
    public void parse_privateParseInt_validAndInvalid() throws Exception {
        Method parseInt = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseInt.setAccessible(true);
        int val = (int) parseInt.invoke(null, "123456", 1, 4);
        assertEquals(234, val);

        // Test invalid number format - expect NumberFormatException wrapped in ParseException
        try {
            parseInt.invoke(null, "12X456", 1, 4);
            fail("Expected NumberFormatException");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof NumberFormatException);
        }
    }

    @Test
    @Timeout(8000)
    public void parse_privateIndexOfNonDigit() throws Exception {
        Method indexOfNonDigit = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigit.setAccessible(true);
        int idx = (int) indexOfNonDigit.invoke(null, "123abc456", 0);
        assertEquals(3, idx);

        idx = (int) indexOfNonDigit.invoke(null, "123456", 0);
        assertEquals(6, idx);
    }

}