package com.google.gson.internal.bind.util;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ISO8601Utils_149_3Test {

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
        int value = 1234;
        int length = 4;

        padIntMethod.invoke(null, buffer, value, length);

        assertEquals("1234", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthLessThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        int value = 7;
        int length = 3;

        padIntMethod.invoke(null, buffer, value, length);

        assertEquals("007", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthGreaterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        int value = 12345;
        int length = 3;

        padIntMethod.invoke(null, buffer, value, length);

        assertEquals("12345", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_zeroValueWithLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        int value = 0;
        int length = 5;

        padIntMethod.invoke(null, buffer, value, length);

        assertEquals("00000", buffer.toString());
    }
}