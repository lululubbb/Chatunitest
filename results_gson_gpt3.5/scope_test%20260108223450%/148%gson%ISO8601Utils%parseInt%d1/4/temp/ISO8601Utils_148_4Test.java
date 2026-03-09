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

public class ISO8601Utils_148_4Test {

    private static Method parseIntMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);
    }

    private int invokeParseInt(String value, int beginIndex, int endIndex) throws Throwable {
        try {
            return (int) parseIntMethod.invoke(null, value, beginIndex, endIndex);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testParseInt_validSingleDigit() throws Throwable {
        assertEquals(3, invokeParseInt("3", 0, 1));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_validMultipleDigits() throws Throwable {
        assertEquals(123, invokeParseInt("123", 0, 3));
        assertEquals(23, invokeParseInt("123", 1, 3));
        assertEquals(2, invokeParseInt("123", 1, 2));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_beginIndexEqualsEndIndex() {
        // empty substring should NOT throw NumberFormatException because no digits processed
        // Instead, it returns 0, so we test for that behavior
        try {
            int result = invokeParseInt("123", 1, 1);
            assertEquals(0, result);
        } catch (Throwable t) {
            fail("Expected no exception but got: " + t);
        }
    }

    @Test
    @Timeout(8000)
    public void testParseInt_beginIndexLessThanZero() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("123", -1, 2);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_endIndexGreaterThanLength() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("123", 0, 4);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_beginIndexGreaterThanEndIndex() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("123", 2, 1);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_invalidCharacterAtStart() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("a123", 0, 4);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_invalidCharacterInMiddle() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("12a3", 0, 4);
        });
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("123a", 0, 4);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_singleZero() throws Throwable {
        assertEquals(0, invokeParseInt("0", 0, 1));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_leadingZeros() throws Throwable {
        assertEquals(7, invokeParseInt("007", 0, 3));
        assertEquals(0, invokeParseInt("000", 0, 3));
    }
}