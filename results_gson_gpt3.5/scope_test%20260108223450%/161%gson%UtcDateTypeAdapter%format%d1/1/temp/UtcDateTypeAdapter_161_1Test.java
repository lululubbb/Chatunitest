package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.typeadapters.UtcDateTypeAdapter;

public class UtcDateTypeAdapter_161_1Test {

    private static Method formatMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        formatMethod = UtcDateTypeAdapter.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
    }

    private String invokeFormat(Date date, boolean millis, TimeZone tz) throws IllegalAccessException, InvocationTargetException {
        return (String) formatMethod.invoke(null, date, millis, tz);
    }

    @Test
    @Timeout(8000)
    public void testFormat_utcWithoutMillis() throws Exception {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(utc, Locale.US);
        cal.set(2023, Calendar.JUNE, 15, 13, 45, 30);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String result = invokeFormat(date, false, utc);
        assertEquals("2023-06-15T13:45:30Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_utcWithMillis() throws Exception {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(utc, Locale.US);
        cal.set(2023, Calendar.JUNE, 15, 13, 45, 30);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String result = invokeFormat(date, true, utc);
        assertEquals("2023-06-15T13:45:30.123Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_nonUtcPositiveOffsetWithoutMillis() throws Exception {
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        Calendar cal = new GregorianCalendar(tz, Locale.US);
        cal.set(2023, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String result = invokeFormat(date, false, tz);
        assertEquals("2023-12-31T23:59:59+02:30", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_nonUtcNegativeOffsetWithMillis() throws Exception {
        TimeZone tz = TimeZone.getTimeZone("GMT-05:45");
        Calendar cal = new GregorianCalendar(tz, Locale.US);
        cal.set(2023, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();

        String result = invokeFormat(date, true, tz);
        assertEquals("2023-01-01T00:00:00.999-05:45", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_monthAndDayPadding() throws Exception {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(utc, Locale.US);
        cal.set(2023, Calendar.JANUARY, 5, 7, 8, 9);
        cal.set(Calendar.MILLISECOND, 5);
        Date date = cal.getTime();

        String result = invokeFormat(date, true, utc);
        assertEquals("2023-01-05T07:08:09.005Z", result);
    }
}