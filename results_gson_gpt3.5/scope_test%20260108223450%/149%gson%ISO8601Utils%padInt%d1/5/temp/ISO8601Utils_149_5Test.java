package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ISO8601Utils_149_5Test {

    private static Method padIntMethod;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        padIntMethod = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthLessThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 42, 5);
        assertEquals("00042", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthEqualsLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 12345, 5);
        assertEquals("12345", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthGreaterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 1234567, 5);
        assertEquals("1234567", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_zeroValueLengthLessThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 0, 3);
        assertEquals("000", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_zeroValueLengthEqualsLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 0, 1);
        assertEquals("0", buffer.toString());
    }
}