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

class ISO8601Utils_149_1Test {

    private static Method padIntMethod;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
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
        // value length = 2, length = 5, expect 3 zeros before value
        padIntMethod.invoke(null, buffer, 42, 5);
        assertEquals("00042", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthGreaterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length = 4, length = 2, no zeros added, just append value
        padIntMethod.invoke(null, buffer, 1234, 2);
        assertEquals("1234", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_zeroValueLengthEqualsLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value = 0, length = 1, expect "0"
        padIntMethod.invoke(null, buffer, 0, 1);
        assertEquals("0", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_zeroValueLengthGreaterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value = 0, length = 0, expect "0" (length < value length)
        padIntMethod.invoke(null, buffer, 0, 0);
        assertEquals("0", buffer.toString());
    }
}