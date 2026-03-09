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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ISO8601Utils_149_6Test {

    private Method padIntMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        padIntMethod = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthEqualsLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length = 3, length = 3, no padding expected
        padIntMethod.invoke(null, buffer, 123, 3);
        assertEquals("123", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthLessThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length = 2, length = 5, expect 3 zeros padded
        padIntMethod.invoke(null, buffer, 45, 5);
        assertEquals("00045", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthGreaterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length = 4, length = 3, no padding, just append value as string
        padIntMethod.invoke(null, buffer, 1234, 3);
        assertEquals("1234", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_zeroValue() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length = 1 (0), length = 4, expect 3 zeros padded
        padIntMethod.invoke(null, buffer, 0, 4);
        assertEquals("0000", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_bufferNotEmpty() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder("prefix");
        // value length = 2, length = 4, expect 2 zeros padded after prefix
        padIntMethod.invoke(null, buffer, 12, 4);
        assertEquals("prefix0012", buffer.toString());
    }
}