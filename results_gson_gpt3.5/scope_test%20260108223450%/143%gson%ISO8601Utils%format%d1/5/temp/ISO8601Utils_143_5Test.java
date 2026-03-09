package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_143_5Test {

    private static Method formatDateMillisTzMethod;
    private static Method checkOffsetMethod;
    private static Method parseIntMethod;
    private static Method padIntMethod;
    private static Method indexOfNonDigitMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        formatDateMillisTzMethod = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatDateMillisTzMethod.setAccessible(true);

        checkOffsetMethod = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffsetMethod.setAccessible(true);

        parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);

        padIntMethod = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);

        indexOfNonDigitMethod = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigitMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testFormat_Date() {
        Date date = new Date(0L); // Epoch
        String expected = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testFormat_DateWithMillisTrue() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L);
        String formatted = (String) formatDateMillisTzMethod.invoke(null, date, true, TimeZone.getTimeZone("UTC"));
        // It should contain milliseconds part
        // e.g. 1970-01-01T00:00:00.000Z
        assertEquals("1970-01-01T00:00:00.000Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormat_DateWithMillisFalse() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L);
        String formatted = (String) formatDateMillisTzMethod.invoke(null, date, false, TimeZone.getTimeZone("UTC"));
        // It should NOT contain milliseconds part
        // e.g. 1970-01-01T00:00:00Z
        assertEquals("1970-01-01T00:00:00Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormat_DateWithNonUTC() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L);
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String formatted = (String) formatDateMillisTzMethod.invoke(null, date, false, tz);
        // Expected output with offset +02:00
        assertEquals("1970-01-01T02:00:00+02:00", formatted);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_True() throws InvocationTargetException, IllegalAccessException {
        String value = "a:b";
        boolean result = (boolean) checkOffsetMethod.invoke(null, value, 1, ':');
        assertEquals(true, result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_False() throws InvocationTargetException, IllegalAccessException {
        String value = "abc";
        boolean result = (boolean) checkOffsetMethod.invoke(null, value, 1, ':');
        assertEquals(false, result);
    }

    @Test
    @Timeout(8000)
    public void testParseInt_Valid() throws InvocationTargetException, IllegalAccessException {
        String value = "1234567890";
        int result = (int) parseIntMethod.invoke(null, value, 2, 5);
        assertEquals(345, result);
    }

    @Test
    @Timeout(8000)
    public void testParseInt_Invalid() {
        String value = "12a456";
        assertThrows(InvocationTargetException.class, () -> {
            parseIntMethod.invoke(null, value, 0, 6);
        });
    }

    @Test
    @Timeout(8000)
    public void testPadInt() throws InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        padIntMethod.invoke(null, sb, 5, 3);
        assertEquals("005", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit() throws InvocationTargetException, IllegalAccessException {
        String str = "12345a6789";
        int index = (int) indexOfNonDigitMethod.invoke(null, str, 0);
        assertEquals(5, index);

        int index2 = (int) indexOfNonDigitMethod.invoke(null, str, 6);
        assertEquals(10, index2);

        int index3 = (int) indexOfNonDigitMethod.invoke(null, "123456789", 0);
        assertEquals(9, index3);
    }
}