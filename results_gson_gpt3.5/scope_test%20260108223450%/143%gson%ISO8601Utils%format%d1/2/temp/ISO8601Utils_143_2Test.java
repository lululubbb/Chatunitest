package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_143_2Test {

    private static Method formatMethodWithMillisAndTZ;

    @BeforeAll
    public static void setup() throws NoSuchMethodException {
        formatMethodWithMillisAndTZ = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethodWithMillisAndTZ.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testFormat_dateOnly() {
        Date date = new Date(0L); // Epoch start
        String expected = "1970-01-01T00:00:00Z";
        String actual = ISO8601Utils.format(date);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testFormat_dateWithMillisTrue() throws Throwable {
        Date date = new Date(123456789L); // Specific timestamp with millis
        String actual = (String) formatMethodWithMillisAndTZ.invoke(null, date, true, TimeZone.getTimeZone("UTC"));
        // Expected string: 1970-01-02T10:17:36.789Z
        assertEquals("1970-01-02T10:17:36.789Z", actual);
    }

    @Test
    @Timeout(8000)
    public void testFormat_dateWithMillisFalse() throws Throwable {
        Date date = new Date(123456789L); // Specific timestamp without millis
        String actual = (String) formatMethodWithMillisAndTZ.invoke(null, date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("1970-01-02T10:17:36Z", actual);
    }

    @Test
    @Timeout(8000)
    public void testFormat_dateWithDifferentTimeZone() throws Throwable {
        Date date = new Date(0L);
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String actual = (String) formatMethodWithMillisAndTZ.invoke(null, date, false, tz);
        // 1970-01-01T02:00:00+02:00
        assertEquals("1970-01-01T02:00:00+02:00", actual);
    }

    @Test
    @Timeout(8000)
    public void testFormat_nullDateThrowsException() {
        assertThrows(NullPointerException.class, () -> ISO8601Utils.format(null));
    }

    @Test
    @Timeout(8000)
    public void testFormat_privateFormatMethod_nullDateThrowsException() {
        assertThrows(InvocationTargetException.class, () -> {
            try {
                formatMethodWithMillisAndTZ.invoke(null, null, false, TimeZone.getTimeZone("UTC"));
            } catch (InvocationTargetException e) {
                // unwrap to check cause
                throw e;
            }
        });
    }
}