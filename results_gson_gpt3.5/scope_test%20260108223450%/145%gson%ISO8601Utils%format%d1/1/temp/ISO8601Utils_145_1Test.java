package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ISO8601Utils_145_1Test {

    private static Method formatMethod;

    @BeforeAll
    static void setUp() throws Exception {
        formatMethod = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
    }

    @Test
    @Timeout(8000)
    void testFormatWithoutMillisUTC() throws Exception {
        Date date = new Date(0L); // Epoch start
        TimeZone tz = TimeZone.getTimeZone("UTC");
        String result = (String) formatMethod.invoke(null, date, false, tz);
        // Expected: 1970-01-01T00:00:00Z
        assertEquals("1970-01-01T00:00:00Z", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithMillisUTC() throws Exception {
        // Specific date with milliseconds
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2023, Calendar.JUNE, 15, 12, 34, 56);
        cal.set(Calendar.MILLISECOND, 789);
        Date date = cal.getTime();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        String result = (String) formatMethod.invoke(null, date, true, tz);
        // Expected: 2023-06-15T12:34:56.789Z
        assertEquals("2023-06-15T12:34:56.789Z", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithoutMillisWithPositiveOffset() throws Exception {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+02:30"));
        cal.set(2022, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        String result = (String) formatMethod.invoke(null, date, false, tz);
        // Expected: 2022-12-31T23:59:59+02:30
        assertEquals("2022-12-31T23:59:59+02:30", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithMillisWithNegativeOffset() throws Exception {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-05:45"));
        cal.set(2021, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();
        TimeZone tz = TimeZone.getTimeZone("GMT-05:45");
        String result = (String) formatMethod.invoke(null, date, true, tz);
        // Expected: 2021-01-01T00:00:00.123-05:45
        assertEquals("2021-01-01T00:00:00.123-05:45", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithZeroOffsetNonUTC() throws Exception {
        // TimeZone with zero offset but not UTC ID (rare but possible)
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2020, Calendar.MARCH, 10, 15, 20, 30);
        cal.set(Calendar.MILLISECOND, 456);
        Date date = cal.getTime();
        String result = (String) formatMethod.invoke(null, date, true, tz);
        // Expected: 2020-03-10T15:20:30.456Z
        assertEquals("2020-03-10T15:20:30.456Z", result);
    }
}