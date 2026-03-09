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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_147_2Test {

    private static Method checkOffsetMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        checkOffsetMethod = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffsetMethod.setAccessible(true);
    }

    private boolean invokeCheckOffset(String value, int offset, char expected) throws InvocationTargetException, IllegalAccessException {
        return (boolean) checkOffsetMethod.invoke(null, value, offset, expected);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetInBounds_CharMatches() throws Exception {
        String value = "2023-06-15T12:34:56Z";
        int offset = 4;
        char expected = '-';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetInBounds_CharDoesNotMatch() throws Exception {
        String value = "2023-06-15T12:34:56Z";
        int offset = 5;
        char expected = 'X';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetEqualsLength() throws Exception {
        String value = "2023-06-15";
        int offset = value.length();
        char expected = '-';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetGreaterThanLength() throws Exception {
        String value = "2023-06-15";
        int offset = value.length() + 1;
        char expected = '-';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_EmptyString() throws Exception {
        String value = "";
        int offset = 0;
        char expected = 'A';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertFalse(result);
    }
}