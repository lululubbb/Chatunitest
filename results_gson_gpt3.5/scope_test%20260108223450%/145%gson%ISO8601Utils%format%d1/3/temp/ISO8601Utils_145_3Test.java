package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ISO8601Utils_145_3Test {

    private static Method padIntMethod;
    private static Method checkOffsetMethod;
    private static Method parseIntMethod;
    private static Method indexOfNonDigitMethod;

    @BeforeAll
    static void setupReflection() throws NoSuchMethodException {
        padIntMethod = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);

        checkOffsetMethod = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffsetMethod.setAccessible(true);

        parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);

        indexOfNonDigitMethod = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigitMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testFormatWithoutMillisUTC() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2023, Calendar.MARCH, 15, 10, 20, 30);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, false, tz);
        assertEquals("2023-03-15T10:20:30Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormatWithMillisUTC() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2023, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        assertEquals("2023-12-31T23:59:59.123Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormatWithPositiveOffset() {
        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2023, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, false, tz);
        // offset +05:30
        assertEquals("2023-01-01T00:00:00+05:30", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormatWithNegativeOffset() {
        TimeZone tz = TimeZone.getTimeZone("GMT-08:00");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2023, Calendar.JUNE, 10, 15, 45, 50);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        assertEquals("2023-06-10T15:45:50.000-08:00", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormatWithZeroMillis() {
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2023, Calendar.APRIL, 5, 8, 9, 10);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = ISO8601Utils.format(date, true, tz);
        assertEquals("2023-04-05T08:09:10.000+02:00", formatted);
    }

    @Test
    @Timeout(8000)
    void testPadIntMethod() throws IllegalAccessException, InvocationTargetException {
        StringBuilder sb = new StringBuilder();
        // padInt(sb, 7, 3) -> "007"
        padIntMethod.invoke(null, sb, 7, 3);
        assertEquals("007", sb.toString());

        sb.setLength(0);
        // padInt(sb, 123, 3) -> "123"
        padIntMethod.invoke(null, sb, 123, 3);
        assertEquals("123", sb.toString());

        sb.setLength(0);
        // padInt(sb, 1234, 3) -> "1234" (no truncation)
        padIntMethod.invoke(null, sb, 1234, 3);
        assertEquals("1234", sb.toString());

        sb.setLength(0);
        // padInt(sb, 0, 2) -> "00"
        padIntMethod.invoke(null, sb, 0, 2);
        assertEquals("00", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testCheckOffsetMethod() throws IllegalAccessException, InvocationTargetException {
        // checkOffset("abc", 1, 'b') -> true
        boolean result = (boolean) checkOffsetMethod.invoke(null, "abc", 1, 'b');
        assertTrue(result);

        // checkOffset("abc", 0, 'a') -> true
        result = (boolean) checkOffsetMethod.invoke(null, "abc", 0, 'a');
        assertTrue(result);

        // checkOffset("abc", 2, 'd') -> false
        result = (boolean) checkOffsetMethod.invoke(null, "abc", 2, 'd');
        assertFalse(result);

        // checkOffset("a", 1, 'b') -> false (index out of bounds)
        result = (boolean) checkOffsetMethod.invoke(null, "a", 1, 'b');
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testParseIntMethod() throws IllegalAccessException, InvocationTargetException {
        // parseInt("12345", 1, 4) -> 234
        int val = (int) parseIntMethod.invoke(null, "12345", 1, 4);
        assertEquals(234, val);

        // parseInt with invalid number -> NumberFormatException
        assertThrows(InvocationTargetException.class, () -> {
            try {
                parseIntMethod.invoke(null, "12a45", 0, 5);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof NumberFormatException) {
                    throw e;
                }
                fail("Expected NumberFormatException");
            }
        });
    }

    @Test
    @Timeout(8000)
    void testIndexOfNonDigitMethod() throws IllegalAccessException, InvocationTargetException {
        // indexOfNonDigit("123abc456", 0) -> 3
        int index = (int) indexOfNonDigitMethod.invoke(null, "123abc456", 0);
        assertEquals(3, index);

        // indexOfNonDigit("123456", 0) -> 6 (length of string)
        index = (int) indexOfNonDigitMethod.invoke(null, "123456", 0);
        assertEquals(6, index);

        // indexOfNonDigit("abc123", 0) -> 0
        index = (int) indexOfNonDigitMethod.invoke(null, "abc123", 0);
        assertEquals(0, index);

        // indexOfNonDigit("123abc", 4) -> 4
        index = (int) indexOfNonDigitMethod.invoke(null, "123abc", 4);
        assertEquals(4, index);
    }
}