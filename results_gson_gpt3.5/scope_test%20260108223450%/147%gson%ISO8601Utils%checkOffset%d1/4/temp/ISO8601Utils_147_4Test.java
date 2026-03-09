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

public class ISO8601Utils_147_4Test {

    private static Method checkOffsetMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        checkOffsetMethod = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffsetMethod.setAccessible(true);
    }

    private boolean invokeCheckOffset(String value, int offset, char expected) {
        try {
            return (boolean) checkOffsetMethod.invoke(null, value, offset, expected);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetLessThanLength_CharMatches() {
        String value = "2023-06-20T12:34:56Z";
        int offset = 4;
        char expected = '-';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetLessThanLength_CharDoesNotMatch() {
        String value = "2023-06-20T12:34:56Z";
        int offset = 4;
        char expected = 'X';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetEqualsLength() {
        String value = "2023-06-20T12:34:56Z";
        int offset = value.length();
        char expected = 'Z';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetGreaterThanLength() {
        String value = "2023-06-20T12:34:56Z";
        int offset = value.length() + 1;
        char expected = 'Z';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_EmptyString() {
        String value = "";
        int offset = 0;
        char expected = 'Z';
        boolean result = invokeCheckOffset(value, offset, expected);
        assertFalse(result);
    }
}