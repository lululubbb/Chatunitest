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

class ISO8601Utils_149_4Test {

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
        // value length == length (3 digits, length 3)
        padIntMethod.invoke(null, buffer, 123, 3);
        assertEquals("123", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthLessThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length < length (7, length 4)
        padIntMethod.invoke(null, buffer, 7, 4);
        assertEquals("0007", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthMuchLessThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length < length (0, length 5)
        padIntMethod.invoke(null, buffer, 0, 5);
        assertEquals("00000", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthGreaterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length > length (12345, length 3)
        padIntMethod.invoke(null, buffer, 12345, 3);
        assertEquals("12345", buffer.toString());
    }

    @Test
    @Timeout(8000)
    void testPadInt_valueLengthOneAndLengthOne() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length == length == 1 (value 9)
        padIntMethod.invoke(null, buffer, 9, 1);
        assertEquals("9", buffer.toString());
    }
}