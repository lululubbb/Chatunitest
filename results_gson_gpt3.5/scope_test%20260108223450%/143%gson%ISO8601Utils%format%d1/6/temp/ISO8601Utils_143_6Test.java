package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ISO8601Utils_143_6Test {

    private static Method format_Date_boolean_TimeZone;
    private static Method format_Date_boolean;
    private static Method parse_String_ParsePosition;
    private static Method checkOffset_String_int_char;
    private static Method parseInt_String_int_int;
    private static Method padInt_StringBuilder_int_int;
    private static Method indexOfNonDigit_String_int;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        format_Date_boolean_TimeZone = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        format_Date_boolean_TimeZone.setAccessible(true);

        format_Date_boolean = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class);
        format_Date_boolean.setAccessible(true);

        parse_String_ParsePosition = ISO8601Utils.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
        parse_String_ParsePosition.setAccessible(true);

        checkOffset_String_int_char = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffset_String_int_char.setAccessible(true);

        parseInt_String_int_int = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseInt_String_int_int.setAccessible(true);

        padInt_StringBuilder_int_int = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padInt_StringBuilder_int_int.setAccessible(true);

        indexOfNonDigit_String_int = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigit_String_int.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testFormat_Date() {
        Date date = new Date(0L); // Epoch
        String result = ISO8601Utils.format(date);
        assertNotNull(result);
        assertTrue(result.startsWith("1970-01-01T00:00:00"));
        assertTrue(result.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    void testFormat_Date_boolean() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L);
        // millis = false
        String noMillis = (String) format_Date_boolean.invoke(null, date, false);
        assertNotNull(noMillis);
        assertFalse(noMillis.contains("."));
        assertTrue(noMillis.endsWith("Z"));

        // millis = true
        String withMillis = (String) format_Date_boolean.invoke(null, date, true);
        assertNotNull(withMillis);
        assertTrue(withMillis.contains(".000"));
        assertTrue(withMillis.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    void testFormat_Date_boolean_TimeZone() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L);
        TimeZone tzUTC = TimeZone.getTimeZone("UTC");
        TimeZone tzGMT8 = TimeZone.getTimeZone("GMT+08:00");

        // millis = false, UTC
        String utcNoMillis = (String) format_Date_boolean_TimeZone.invoke(null, date, false, tzUTC);
        assertNotNull(utcNoMillis);
        assertTrue(utcNoMillis.endsWith("Z"));

        // millis = true, UTC
        String utcWithMillis = (String) format_Date_boolean_TimeZone.invoke(null, date, true, tzUTC);
        assertNotNull(utcWithMillis);
        assertTrue(utcWithMillis.contains(".000"));
        assertTrue(utcWithMillis.endsWith("Z"));

        // millis = false, GMT+8
        String gmt8NoMillis = (String) format_Date_boolean_TimeZone.invoke(null, date, false, tzGMT8);
        assertNotNull(gmt8NoMillis);
        assertTrue(gmt8NoMillis.startsWith("1970-01-01T08:00:00"));

        // millis = true, GMT+8
        String gmt8WithMillis = (String) format_Date_boolean_TimeZone.invoke(null, date, true, tzGMT8);
        assertNotNull(gmt8WithMillis);
        assertTrue(gmt8WithMillis.contains(".000"));
        assertTrue(gmt8WithMillis.startsWith("1970-01-01T08:00:00"));
    }

    @Test
    @Timeout(8000)
    void testParse_ValidAndInvalid() throws InvocationTargetException, IllegalAccessException {
        String valid = "1970-01-01T00:00:00Z";
        ParsePosition pos = new ParsePosition(0);
        Date date = (Date) parse_String_ParsePosition.invoke(null, valid, pos);
        assertNotNull(date);
        assertEquals(valid.length(), pos.getIndex());

        // Invalid string - should return null and pos index unchanged
        String invalid = "invalid-date";
        ParsePosition posInvalid = new ParsePosition(0);
        Date dateInvalid = null;
        try {
            dateInvalid = (Date) parse_String_ParsePosition.invoke(null, invalid, posInvalid);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof java.text.ParseException) {
                // Expected exception for invalid input, treat as parse failure
                dateInvalid = null;
                // posInvalid index should remain unchanged (0)
            } else {
                throw e;
            }
        }
        assertNull(dateInvalid);
        assertEquals(0, posInvalid.getIndex());
    }

    @Test
    @Timeout(8000)
    void testCheckOffset() throws InvocationTargetException, IllegalAccessException {
        String s = "abc-def";
        // offset in bounds and char matches
        boolean resultTrue = (boolean) checkOffset_String_int_char.invoke(null, s, 3, '-');
        assertTrue(resultTrue);

        // offset in bounds but char does not match
        boolean resultFalse1 = (boolean) checkOffset_String_int_char.invoke(null, s, 3, 'x');
        assertFalse(resultFalse1);

        // offset out of bounds
        boolean resultFalse2 = (boolean) checkOffset_String_int_char.invoke(null, s, s.length(), '-');
        assertFalse(resultFalse2);
    }

    @Test
    @Timeout(8000)
    void testParseInt() throws InvocationTargetException, IllegalAccessException {
        String s = "1234567890";

        // normal parse
        int val = (int) parseInt_String_int_int.invoke(null, s, 2, 5);
        assertEquals(345, val);

        // parse single digit
        int single = (int) parseInt_String_int_int.invoke(null, s, 0, 1);
        assertEquals(1, single);

        // parse full string
        int full = (int) parseInt_String_int_int.invoke(null, s, 0, s.length());
        assertEquals(1234567890, full);
    }

    @Test
    @Timeout(8000)
    void testPadInt() throws InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        // pad 5 to length 3 -> "005"
        padInt_StringBuilder_int_int.invoke(null, sb, 5, 3);
        assertEquals("005", sb.toString());

        sb.setLength(0);
        // pad 123 to length 2 -> "123" (no truncation)
        padInt_StringBuilder_int_int.invoke(null, sb, 123, 2);
        assertEquals("123", sb.toString());

        sb.setLength(0);
        // pad 0 to length 4 -> "0000"
        padInt_StringBuilder_int_int.invoke(null, sb, 0, 4);
        assertEquals("0000", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testIndexOfNonDigit() throws InvocationTargetException, IllegalAccessException {
        String s = "12345abc678";
        // first non-digit at index 5
        int idx = (int) indexOfNonDigit_String_int.invoke(null, s, 0);
        assertEquals(5, idx);

        // start searching at 6, first non-digit at 6 is 'a' (index 5 is 'a', so 6 is 'b' non-digit)
        // Actually at index 6 is 'b' which is non-digit, so expect 6
        int idx2 = (int) indexOfNonDigit_String_int.invoke(null, s, 6);
        assertEquals(6, idx2);

        // start at length (out of bounds)
        int idx3 = (int) indexOfNonDigit_String_int.invoke(null, s, s.length());
        assertEquals(s.length(), idx3);
    }
}