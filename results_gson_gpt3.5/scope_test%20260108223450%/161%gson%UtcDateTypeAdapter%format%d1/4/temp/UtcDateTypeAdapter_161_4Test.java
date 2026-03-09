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

public class UtcDateTypeAdapter_161_4Test {

    private static Method formatMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        formatMethod = UtcDateTypeAdapter.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testFormatUtcNoMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(utc);
        cal.set(2024, Calendar.JUNE, 15, 12, 30, 45);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, false, utc);
        // Expected pattern: yyyy-MM-ddThh:mm:ssZ (Z because offset=0)
        assertEquals("2024-06-15T12:30:45Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatUtcWithMillis() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(utc);
        cal.set(2024, Calendar.JUNE, 15, 12, 30, 45);
        cal.set(Calendar.MILLISECOND, 123);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, true, utc);
        // Expected pattern: yyyy-MM-ddThh:mm:ss.sssZ
        assertEquals("2024-06-15T12:30:45.123Z", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatNonUtcPositiveOffsetNoMillis() throws InvocationTargetException, IllegalAccessException {
        // GMT+02:30 (offset 2*3600000 + 30*60000 = 9000000 ms)
        TimeZone tz = TimeZone.getTimeZone("GMT+02:30");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2024, Calendar.JUNE, 15, 12, 30, 45);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, false, tz);
        // Expected pattern: yyyy-MM-ddThh:mm:ss+02:30
        assertEquals("2024-06-15T12:30:45+02:30", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatNonUtcNegativeOffsetWithMillis() throws InvocationTargetException, IllegalAccessException {
        // GMT-05:45 (offset -5*3600000 - 45*60000 = -20700000 ms)
        TimeZone tz = TimeZone.getTimeZone("GMT-05:45");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(2024, Calendar.JUNE, 15, 12, 30, 45);
        cal.set(Calendar.MILLISECOND, 789);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, true, tz);
        // Expected pattern: yyyy-MM-ddThh:mm:ss.sss-05:45
        assertEquals("2024-06-15T12:30:45.789-05:45", formatted);
    }

    @Test
    @Timeout(8000)
    public void testFormatEdgeCases() throws InvocationTargetException, IllegalAccessException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        // Test year padding with year < 1000 (e.g. year 5)
        Calendar cal = Calendar.getInstance(utc);
        cal.set(5, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();

        String formatted = (String) formatMethod.invoke(null, date, false, utc);
        // year padded to 4 digits: 0005-01-01T00:00:00Z
        assertEquals("0005-01-01T00:00:00Z", formatted);
    }
}