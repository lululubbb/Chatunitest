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

public class ISO8601Utils_149_2Test {

    private static Method padIntMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        padIntMethod = ISO8601Utils.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueShorterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 7, 3);
        assertEquals("007", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueEqualToLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 123, 3);
        assertEquals("123", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLongerThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 12345, 3);
        assertEquals("12345", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_zeroValueWithLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 0, 4);
        assertEquals("0000", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_zeroValueWithZeroLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        padIntMethod.invoke(null, buffer, 0, 0);
        assertEquals("0", buffer.toString());
    }
}