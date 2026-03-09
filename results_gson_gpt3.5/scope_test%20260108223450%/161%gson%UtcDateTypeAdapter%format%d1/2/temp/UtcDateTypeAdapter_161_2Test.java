package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.GregorianCalendar;
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
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_161_2Test {

    private static Method formatMethod;

    @BeforeAll
    public static void setup() throws NoSuchMethodException {
        formatMethod = UtcDateTypeAdapter.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
    }

    private String invokeFormat(Date date, boolean millis, TimeZone tz) throws InvocationTargetException, IllegalAccessException {
        return (String) formatMethod.invoke(null, date, millis, tz);
    }

    @Test
    @Timeout(8000)
    public void testFormatUtcWithoutMillis() throws Exception {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2023, Calendar.JUNE, 15, 13, 45, 30);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = invokeFormat(date, false, tz);
        // Expected format: yyyy-MM-ddThh:mm:ssZ
        assertEquals("2023-06-15T13:45:30Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatUtcWithMillis() throws Exception {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2023, Calendar.JUNE, 15, 13, 45, 30);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String formatted = invokeFormat(date, true, tz);
        // Expected format: yyyy-MM-ddThh:mm:ss.sssZ
        assertEquals("2023-06-15T13:45:30.123Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatNonUtcWithPositiveOffsetWithoutMillis() throws Exception {
        // TimeZone with +02:30 offset (e.g. Asia/Tehran has +3:30 but let's create custom)
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2023, Calendar.MARCH, 10, 8, 5, 9);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = invokeFormat(date, false, tz);
        // Expected format: yyyy-MM-ddThh:mm:ss+hh:mm with offset +02:30
        assertEquals("2023-03-10T08:05:09+02:30", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatNonUtcWithNegativeOffsetWithMillis() throws Exception {
        // TimeZone with -05:45 offset (e.g. Nepal Standard Time)
        TimeZone tz = TimeZone.getTimeZone("GMT-05:45");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2023, Calendar.DECEMBER, 31, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();

        String formatted = invokeFormat(date, true, tz);
        // Expected format: yyyy-MM-ddThh:mm:ss.sss-hh:mm with offset -05:45
        assertEquals("2023-12-31T23:59:59.999-05:45", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatZeroMillisWithMillisTrue() throws Exception {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = invokeFormat(date, true, tz);
        assertEquals("2022-01-01T00:00:00.000Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatMonthAndDaySingleDigit() throws Exception {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.clear();
        cal.set(2021, Calendar.APRIL, 5, 9, 7, 3);
        cal.set(Calendar.MILLISECOND, 45);
        Date date = cal.getTime();

        String formatted = invokeFormat(date, true, tz);
        assertEquals("2021-04-05T09:07:03.045Z", formatted);
    }
}