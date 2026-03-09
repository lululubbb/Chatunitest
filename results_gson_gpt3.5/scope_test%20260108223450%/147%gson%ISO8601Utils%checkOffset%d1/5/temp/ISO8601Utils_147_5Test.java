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
import org.junit.jupiter.api.Test;

public class ISO8601Utils_147_5Test {

    @Test
    @Timeout(8000)
    void testCheckOffset_OffsetLessThanLength_CharMatches() throws Exception {
        Method checkOffset = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffset.setAccessible(true);

        String value = "2023-06-01T12:00:00Z";
        int offset = 4;
        char expected = '-';

        boolean result = (boolean) checkOffset.invoke(null, value, offset, expected);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testCheckOffset_OffsetLessThanLength_CharDoesNotMatch() throws Exception {
        Method checkOffset = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffset.setAccessible(true);

        String value = "20230601T12:00:00Z";
        int offset = 4;
        char expected = '-';

        boolean result = (boolean) checkOffset.invoke(null, value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testCheckOffset_OffsetEqualsLength() throws Exception {
        Method checkOffset = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffset.setAccessible(true);

        String value = "2023-06-01";
        int offset = value.length();
        char expected = '1';

        boolean result = (boolean) checkOffset.invoke(null, value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testCheckOffset_OffsetGreaterThanLength() throws Exception {
        Method checkOffset = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffset.setAccessible(true);

        String value = "2023-06-01";
        int offset = value.length() + 1;
        char expected = '1';

        boolean result = (boolean) checkOffset.invoke(null, value, offset, expected);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testCheckOffset_EmptyString() throws Exception {
        Method checkOffset = ISO8601Utils.class.getDeclaredMethod("checkOffset", String.class, int.class, char.class);
        checkOffset.setAccessible(true);

        String value = "";
        int offset = 0;
        char expected = 'a';

        boolean result = (boolean) checkOffset.invoke(null, value, offset, expected);
        assertFalse(result);
    }
}