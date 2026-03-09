package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ISO8601Utils_144_1Test {

    private static Method formatMethodWithTz;
    private static Method formatMethodWithMillis;

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        formatMethodWithTz = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethodWithTz.setAccessible(true);

        formatMethodWithMillis = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class);
        formatMethodWithMillis.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testFormatDateWithMillisTrue() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L); // Epoch
        String result = (String) formatMethodWithMillis.invoke(null, date, true);
        assertNotNull(result);
        // The string should contain milliseconds
        assertTrue(result.contains(".000"));
        assertTrue(result.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    void testFormatDateWithMillisFalse() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L); // Epoch
        String result = (String) formatMethodWithMillis.invoke(null, date, false);
        assertNotNull(result);
        // The string should NOT contain milliseconds
        assertFalse(result.contains(".000"));
        assertTrue(result.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    void testFormatDateWithTimeZoneUTC() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L); // Epoch
        String result = (String) formatMethodWithTz.invoke(null, date, true, TimeZone.getTimeZone("UTC"));
        assertNotNull(result);
        assertTrue(result.contains(".000"));
        assertTrue(result.endsWith("Z"));
    }

    @Test
    @Timeout(8000)
    void testFormatDateWithTimeZoneNonUTC() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L); // Epoch
        // Timezone offset +02:00
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String result = (String) formatMethodWithTz.invoke(null, date, true, tz);
        assertNotNull(result);
        assertTrue(result.contains(".000"));
        // Should not end with Z, but with +02:00
        assertFalse(result.endsWith("Z"));
        assertTrue(result.endsWith("+02:00"));
    }

    @Test
    @Timeout(8000)
    void testFormatDateWithNullDateThrowsException() {
        Throwable thrown = assertThrows(InvocationTargetException.class, () -> {
            formatMethodWithMillis.invoke(null, (Date) null, true);
        });
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof NullPointerException);
    }
}