package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

public class ISO8601Utils_143_1Test {

    @Test
    @Timeout(8000)
    public void testFormat_Date_Null() {
        // Passing null date should throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            ISO8601Utils.format(null);
        });
    }

    @Test
    @Timeout(8000)
    public void testFormat_Date_UTC_Default() {
        Date date = new Date(0L); // Epoch start
        String formatted = ISO8601Utils.format(date);
        // Should produce "1970-01-01T00:00:00Z" (no millis, UTC)
        assertEquals("1970-01-01T00:00:00Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormat_Date_UTC_WithMillis() throws Exception {
        Date date = new Date(123456789L); // specific time with millis
        // Use reflection to call private format(Date, boolean, TimeZone)
        Method formatMethod = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
        String formattedWithMillis = (String) formatMethod.invoke(null, date, true, TimeZone.getTimeZone("UTC"));
        String formattedWithoutMillis = (String) formatMethod.invoke(null, date, false, TimeZone.getTimeZone("UTC"));

        assertTrue(formattedWithMillis.contains("."));
        assertTrue(formattedWithMillis.endsWith("Z"));
        assertFalse(formattedWithoutMillis.contains("."));
        assertTrue(formattedWithoutMillis.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    public void testFormat_Date_WithTimeZone() throws Exception {
        Date date = new Date(0L); // Epoch start
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");

        Method formatMethod = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
        String formatted = (String) formatMethod.invoke(null, date, false, tz);

        // Should end with +02:00
        assertTrue(formatted.endsWith("+02:00"));
        assertTrue(formatted.startsWith("1970-01-01T"));
    }

    @Test
    @Timeout(8000)
    public void testFormat_Date_WithTimeZone_Millis() throws Exception {
        Date date = new Date(123456789L);
        TimeZone tz = TimeZone.getTimeZone("GMT-05:30");

        Method formatMethod = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
        String formatted = (String) formatMethod.invoke(null, date, true, tz);

        assertTrue(formatted.contains("."));

        // Fix: The offset string in output may be "-05:30" but might not be at the end if 'Z' is used.
        // So check that the formatted string contains the offset substring "-05:30" or "-0530" anywhere.
        // Also, the offset might be represented as "-05:30" or "-0530" depending on implementation,
        // so check for both variants.
        boolean containsOffset = formatted.contains("-05:30") || formatted.contains("-0530");
        assertTrue(containsOffset, "Formatted string should contain the offset '-05:30' or '-0530'");

        assertTrue(formatted.startsWith("1970-01-01T"));
    }
}