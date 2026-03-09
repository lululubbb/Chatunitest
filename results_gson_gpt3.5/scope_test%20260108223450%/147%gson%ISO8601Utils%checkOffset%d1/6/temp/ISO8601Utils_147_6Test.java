package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ISO8601Utils_147_6Test {

    @Test
    @Timeout(8000)
    void testCheckOffset_OffsetInRange_CharMatches() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        method.setAccessible(true);

        String value = "2023-06-10T12:34:56Z";
        int offset = 4;
        char expected = '-';

        boolean result = (boolean) method.invoke(null, value, offset, expected);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testCheckOffset_OffsetInRange_CharDoesNotMatch() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        method.setAccessible(true);

        String value = "2023-06-10T12:34:56Z";
        int offset = 5;
        char expected = '-';

        boolean result = (boolean) method.invoke(null, value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testCheckOffset_OffsetEqualsLength() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        method.setAccessible(true);

        String value = "2023-06-10";
        int offset = value.length();
        char expected = 'X';

        boolean result = (boolean) method.invoke(null, value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testCheckOffset_OffsetGreaterThanLength() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        method.setAccessible(true);

        String value = "2023-06-10";
        int offset = value.length() + 1;
        char expected = 'X';

        boolean result = (boolean) method.invoke(null, value, offset, expected);
        assertFalse(result);
    }

}