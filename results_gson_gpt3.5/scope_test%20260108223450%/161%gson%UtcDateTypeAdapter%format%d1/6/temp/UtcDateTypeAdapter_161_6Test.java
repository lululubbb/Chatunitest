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

public class UtcDateTypeAdapter_161_6Test {

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
        Calendar cal = Calendar.getInstance(utc, Locale.US);
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, Calendar.JULY); // fixed month to JULY (6)
        cal.set(Calendar.DAY_OF_MONTH, 15);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 30);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, false, utc);
        // Expected format: 2023-07-15T13:45:30Z (month +1)
        assertEquals("2023-07-15T13:45:30Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatUtcWithMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(utc, Locale.US);
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, Calendar.JULY); // fixed month to JULY (6)
        cal.set(Calendar.DAY_OF_MONTH, 15);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 30);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, true, utc);
        // Expected format: 2023-07-15T13:45:30.123Z
        assertEquals("2023-07-15T13:45:30.123Z", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatNonUtcPositiveOffsetWithoutMillis() throws InvocationTargetException, IllegalAccessException {
        // TimeZone GMT+02:30 (offset 2h30m)
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, Calendar.JULY); // fixed month to JULY (6)
        cal.set(Calendar.DAY_OF_MONTH, 15);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 30);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, false, tz);
        // Expected format: 2023-07-15T13:45:30+02:30
        assertEquals("2023-07-15T13:45:30+02:30", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatNonUtcNegativeOffsetWithMillis() throws InvocationTargetException, IllegalAccessException {
        // TimeZone GMT-05:45 (offset -5h45m)
        TimeZone tz = TimeZone.getTimeZone("GMT-05:45");
        Calendar cal = Calendar.getInstance(tz, Locale.US);
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, true, tz);
        // Expected format: 2023-12-31T23:59:59.999-05:45
        assertEquals("2023-12-31T23:59:59.999-05:45", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatZeroMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(utc, Locale.US);
        cal.set(Calendar.YEAR, 2024);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String result = (String) formatMethod.invoke(null, date, true, utc);
        // Expected format: 2024-01-01T00:00:00.000Z
        assertEquals("2024-01-01T00:00:00.000Z", result);
    }
}