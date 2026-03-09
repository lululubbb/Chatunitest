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

class ISO8601Utils_145_6Test {

    private static Method formatMethod;

    @BeforeAll
    static void setup() throws Exception {
        formatMethod = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testFormatWithoutMillisUtc() throws Exception {
        Date date = new Date(0L); // Epoch
        TimeZone tz = TimeZone.getTimeZone("UTC");
        String result = (String) formatMethod.invoke(null, date, false, tz);
        // Expected: 1970-01-01T00:00:00Z
        assertEquals("1970-01-01T00:00:00Z", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithMillisUtc() throws Exception {
        Date date = new Date(1234L); // 1.234 seconds after epoch
        TimeZone tz = TimeZone.getTimeZone("UTC");
        String result = (String) formatMethod.invoke(null, date, true, tz);
        // Expected: 1970-01-01T00:00:01.234Z
        assertEquals("1970-01-01T00:00:01.234Z", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithoutMillisNonUtcPositiveOffset() throws Exception {
        // TimeZone offset +02:30 (e.g. Asia/Tehran is +03:30 but let's create custom)
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        // Date set to 1970-01-01T00:00:00 UTC
        Date date = new Date(0L);
        String result = (String) formatMethod.invoke(null, date, false, tz);
        // The local time in tz is 1970-01-01T02:30:00+02:30
        assertEquals("1970-01-01T02:30:00+02:30", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithMillisNonUtcNegativeOffset() throws Exception {
        // TimeZone offset -05:00 (e.g. EST)
        TimeZone tz = TimeZone.getTimeZone("GMT-05:00");
        // Date set to 1970-01-01T00:00:00 UTC
        Date date = new Date(0L);
        String result = (String) formatMethod.invoke(null, date, true, tz);
        // Local time is 1969-12-31T19:00:00.000-05:00
        assertEquals("1969-12-31T19:00:00.000-05:00", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithMillisNonUtcPositiveOffsetWithMillis() throws Exception {
        // TimeZone offset +01:00
        TimeZone tz = TimeZone.getTimeZone("GMT+01:00");
        // Date with milliseconds 3723007 (1 hour 2 minutes 3 seconds and 7 ms after epoch)
        // Adjust date to UTC so that local time in +01:00 is 01:02:03.007
        Date date = new Date(3723007L - (1 * 60 * 60 * 1000L));
        String result = (String) formatMethod.invoke(null, date, true, tz);
        // Local time: 1970-01-01T01:02:03.007+01:00
        assertEquals("1970-01-01T01:02:03.007+01:00", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithMillisNonUtcZeroOffsetButNonUtcTimeZone() throws Exception {
        // Create a timezone with zero offset but non-UTC ID
        TimeZone tz = TimeZone.getTimeZone("GMT");
        // Date with some time
        Date date = new Date(3600000L); // 1 hour after epoch
        String result = (String) formatMethod.invoke(null, date, true, tz);
        // Should end with Z
        assertTrue(result.endsWith("Z"));
        // Should be 1970-01-01T01:00:00.000Z
        assertEquals("1970-01-01T01:00:00.000Z", result);
    }
}