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

public class ISO8601Utils_144_2Test {

    private static Method formatMethodWithMillisTz;

    @BeforeAll
    public static void setup() throws NoSuchMethodException {
        formatMethodWithMillisTz = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethodWithMillisTz.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testFormat_DateMillis_UTC() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L); // Epoch start
        String result = (String) formatMethodWithMillisTz.invoke(null, date, true, TimeZone.getTimeZone("UTC"));
        // Expected: 1970-01-01T00:00:00.000Z
        assertEquals("1970-01-01T00:00:00.000Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_DateNoMillis_UTC() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L);
        String result = (String) formatMethodWithMillisTz.invoke(null, date, false, TimeZone.getTimeZone("UTC"));
        // Expected: 1970-01-01T00:00:00Z
        assertEquals("1970-01-01T00:00:00Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_DateMillis_NonUTC() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L);
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        String result = (String) formatMethodWithMillisTz.invoke(null, date, true, tz);
        // Expected: 1970-01-01T02:00:00.000+02:00
        assertEquals("1970-01-01T02:00:00.000+02:00", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_DateNoMillis_NonUTC() throws InvocationTargetException, IllegalAccessException {
        Date date = new Date(0L);
        TimeZone tz = TimeZone.getTimeZone("GMT-05:00");
        String result = (String) formatMethodWithMillisTz.invoke(null, date, false, tz);
        // Expected: 1969-12-31T19:00:00-05:00
        assertEquals("1969-12-31T19:00:00-05:00", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_PublicFormatDateMillis() {
        Date date = new Date(0L);
        String result = ISO8601Utils.format(date, true);
        assertEquals("1970-01-01T00:00:00.000Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_PublicFormatDateNoMillis() {
        Date date = new Date(0L);
        String result = ISO8601Utils.format(date);
        assertEquals("1970-01-01T00:00:00Z", result);
    }
}