package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.GregorianCalendar;
import java.util.Locale;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UtcDateTypeAdapterFormatTest {

    private static Method formatMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        formatMethod = UtcDateTypeAdapter.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testFormatUtcWithoutMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(utc);
        cal.clear();
        cal.set(2023, Calendar.MARCH, 15, 10, 20, 30);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, false, utc);
        assertEquals("2023-03-15T10:20:30Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatUtcWithMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(utc);
        cal.clear();
        cal.set(2023, Calendar.MARCH, 15, 10, 20, 30);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, true, utc);
        assertEquals("2023-03-15T10:20:30.123Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithPositiveOffsetNoMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        Calendar cal = Calendar.getInstance(tz);
        cal.clear();
        cal.set(2023, Calendar.DECEMBER, 25, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, false, tz);
        assertEquals("2023-12-25T23:59:59+02:30", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNegativeOffsetWithMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone tz = TimeZone.getTimeZone("GMT-05:45");
        Calendar cal = Calendar.getInstance(tz);
        cal.clear();
        cal.set(2023, Calendar.JULY, 4, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, true, tz);
        assertEquals("2023-07-04T12:00:00.999-05:45", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithZeroOffsetMillisFalse() throws InvocationTargetException, IllegalAccessException {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(tz);
        cal.clear();
        cal.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, false, tz);
        assertEquals("2024-01-01T00:00:00Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithZeroOffsetMillisTrue() throws InvocationTargetException, IllegalAccessException {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Calendar cal = Calendar.getInstance(tz);
        cal.clear();
        cal.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 1);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, true, tz);
        assertEquals("2024-01-01T00:00:00.001Z", formatted);
    }
}