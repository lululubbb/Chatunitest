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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_161_5Test {

    private static Method formatMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        formatMethod = UtcDateTypeAdapter.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testFormat_UTC_noMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(utc, Locale.US);
        cal.set(2023, Calendar.APRIL, 15, 13, 45, 30);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, false, utc);

        // Expected format: yyyy-MM-ddThh:mm:ssZ
        assertEquals("2023-04-15T13:45:30Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_UTC_withMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(utc, Locale.US);
        cal.set(2023, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, true, utc);

        // Expected format: yyyy-MM-ddThh:mm:ss.sssZ
        assertEquals("2023-12-31T23:59:59.123Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_NonUTC_PositiveOffset_noMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        Calendar cal = new GregorianCalendar(tz, Locale.US);
        cal.set(2023, Calendar.JANUARY, 1, 10, 20, 30);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, false, tz);

        // Expected format: yyyy-MM-ddThh:mm:ss+hh:mm
        assertEquals("2023-01-01T10:20:30+02:30", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_NonUTC_NegativeOffset_withMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone tz = TimeZone.getTimeZone("GMT-05:45");
        Calendar cal = new GregorianCalendar(tz, Locale.US);
        cal.set(2023, Calendar.JULY, 4, 5, 6, 7);
        cal.set(Calendar.MILLISECOND, 9);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, true, tz);

        // Expected format: yyyy-MM-ddThh:mm:ss.sss-hh:mm
        assertEquals("2023-07-04T05:06:07.009-05:45", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_ZeroMillis_padding() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(utc, Locale.US);
        cal.set(2024, Calendar.MARCH, 10, 1, 2, 3);
        cal.set(Calendar.MILLISECOND, 7);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, true, utc);

        // Milliseconds should be zero-padded to 3 digits
        assertEquals("2024-03-10T01:02:03.007Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_MonthDayHourMinuteSecondPadding() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(utc, Locale.US);
        cal.set(2024, Calendar.JANUARY, 1, 1, 2, 3);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, false, utc);

        // All numeric fields should be zero-padded to 2 digits except year
        assertEquals("2024-01-01T01:02:03Z", result);
    }
}