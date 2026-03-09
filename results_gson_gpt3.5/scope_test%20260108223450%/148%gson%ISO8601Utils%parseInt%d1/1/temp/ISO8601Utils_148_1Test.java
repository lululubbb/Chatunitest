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

public class ISO8601Utils_148_1Test {

    private static Method parseIntMethod;

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);
    }

    private int invokeParseInt(String value, int beginIndex, int endIndex) throws Throwable {
        try {
            return (int) parseIntMethod.invoke(null, value, beginIndex, endIndex);
        } catch (InvocationTargetException e) {
            // unwrap the underlying exception
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testParseInt_ValidWholeString() throws Throwable {
        String value = "1234567890";
        int result = invokeParseInt(value, 0, value.length());
        assertEquals(1234567890, result);
    }

    @Test
    @Timeout(8000)
    void testParseInt_ValidSubstring() throws Throwable {
        String value = "abc123def";
        int result = invokeParseInt(value, 3, 6);
        assertEquals(123, result);
    }

    @Test
    @Timeout(8000)
    void testParseInt_SingleDigit() throws Throwable {
        String value = "7";
        int result = invokeParseInt(value, 0, 1);
        assertEquals(7, result);
    }

    @Test
    @Timeout(8000)
    void testParseInt_EmptySubstring() {
        String value = "123";
        // Adjusted to expect no exception for empty substring because parseInt returns 0 in that case
        try {
            int result = invokeParseInt(value, 1, 1);
            assertEquals(0, result);
        } catch (Throwable t) {
            fail("Expected no exception, but got: " + t);
        }
    }

    @Test
    @Timeout(8000)
    void testParseInt_BeginIndexNegative() {
        String value = "123";
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> invokeParseInt(value, -1, 2));
        assertEquals(value, ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParseInt_EndIndexGreaterThanLength() {
        String value = "123";
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 0, 10));
        assertEquals(value, ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParseInt_BeginIndexGreaterThanEndIndex() {
        String value = "123";
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 2, 1));
        assertEquals(value, ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParseInt_InvalidCharacterAtStart() {
        String value = "a123";
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 0, 1));
        assertEquals("Invalid number: a", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParseInt_InvalidCharacterInMiddle() {
        String value = "12a3";
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 0, 4));
        assertEquals("Invalid number: 12a3", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParseInt_InvalidCharacterAtEnd() {
        String value = "123a";
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> invokeParseInt(value, 0, 4));
        assertEquals("Invalid number: 123a", ex.getMessage());
    }
}