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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ISO8601Utils_147_3Test {

    private static Method checkOffsetMethod;

    @BeforeAll
    public static void setUp() throws Exception {
        checkOffsetMethod = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffsetMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetLessThanLength_CharMatches() throws Exception {
        String value = "2023-06-15T12:34:56Z";
        int offset = 4;
        char expected = '-';
        boolean result = (boolean) checkOffsetMethod.invoke(null, value, offset, expected);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetLessThanLength_CharDoesNotMatch() throws Exception {
        String value = "2023-06-15T12:34:56Z";
        int offset = 4;
        char expected = 'X';
        boolean result = (boolean) checkOffsetMethod.invoke(null, value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetEqualsLength() throws Exception {
        String value = "2023-06-15T12:34:56Z";
        int offset = value.length();
        char expected = 'Z';
        boolean result = (boolean) checkOffsetMethod.invoke(null, value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_OffsetGreaterThanLength() throws Exception {
        String value = "2023-06-15T12:34:56Z";
        int offset = value.length() + 1;
        char expected = 'Z';
        boolean result = (boolean) checkOffsetMethod.invoke(null, value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testCheckOffset_EmptyString() throws Exception {
        String value = "";
        int offset = 0;
        char expected = 'Z';
        boolean result = (boolean) checkOffsetMethod.invoke(null, value, offset, expected);
        assertFalse(result);
    }
}