package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ISO8601Utils_144_5Test {

    private Date date;
    private TimeZone tzUtc;
    private TimeZone tzGmtPlus2;

    @BeforeEach
    void setUp() {
        // Date: 2024-06-15T12:34:56.789Z
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2024, Calendar.JUNE, 15, 12, 34, 56);
        cal.set(Calendar.MILLISECOND, 789);
        date = cal.getTime();
        tzUtc = TimeZone.getTimeZone("UTC");
        tzGmtPlus2 = TimeZone.getTimeZone("GMT+02:00");
    }

    @Test
    @Timeout(8000)
    void testFormat_dateMillisTrue_usesUTC() {
        String formatted = ISO8601Utils.format(date, true);
        // Expected: 2024-06-15T12:34:56.789Z
        assertEquals("2024-06-15T12:34:56.789Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_dateMillisFalse_usesUTC() {
        String formatted = ISO8601Utils.format(date, false);
        // Expected: 2024-06-15T12:34:56Z (no millis)
        assertEquals("2024-06-15T12:34:56Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_dateMillisTrue_withNonUTCtz() {
        // Use GMT+2 timezone, millis true
        String formatted = ISO8601Utils.format(date, true, tzGmtPlus2);
        // 12:34:56.789 UTC is 14:34:56.789 GMT+02:00
        assertEquals("2024-06-15T14:34:56.789+02:00", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_dateMillisFalse_withNonUTCtz() {
        // Use GMT+2 timezone, millis false
        String formatted = ISO8601Utils.format(date, false, tzGmtPlus2);
        // 12:34:56 UTC is 14:34:56 GMT+02:00
        assertEquals("2024-06-15T14:34:56+02:00", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_dateMillisTrue_withUTCtzExplicit() {
        // Explicitly pass UTC timezone
        String formatted = ISO8601Utils.format(date, true, tzUtc);
        assertEquals("2024-06-15T12:34:56.789Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_dateMillisFalse_withUTCtzExplicit() {
        String formatted = ISO8601Utils.format(date, false, tzUtc);
        assertEquals("2024-06-15T12:34:56Z", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormat_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ISO8601Utils.format(null, true));
    }

    @Test
    @Timeout(8000)
    void testInvokePrivateFormat_DateMillisTimeZone() throws Exception {
        Method formatMethod = ISO8601Utils.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);

        // millis true, UTC
        String result = (String) formatMethod.invoke(null, date, true, tzUtc);
        assertEquals("2024-06-15T12:34:56.789Z", result);

        // millis false, GMT+2
        result = (String) formatMethod.invoke(null, date, false, tzGmtPlus2);
        assertEquals("2024-06-15T14:34:56+02:00", result);
    }
}