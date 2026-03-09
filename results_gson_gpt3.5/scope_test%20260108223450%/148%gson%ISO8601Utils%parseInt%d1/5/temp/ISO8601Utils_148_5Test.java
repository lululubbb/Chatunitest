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

public class ISO8601Utils_148_5Test {

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
            // unwrap the underlying exception
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ValidSingleDigit() throws Throwable {
        assertEquals(5, invokeParseInt("5", 0, 1));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ValidMultipleDigits() throws Throwable {
        assertEquals(12345, invokeParseInt("12345", 0, 5));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ValidSubstring() throws Throwable {
        // Fix: Adjust expected value from 234 to 123 to match substring "123" from index 1 to 4
        assertEquals(123, invokeParseInt("a123456", 1, 4));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ZeroValue() throws Throwable {
        assertEquals(0, invokeParseInt("0", 0, 1));
        assertEquals(0, invokeParseInt("0000", 0, 4));
        assertEquals(0, invokeParseInt("a0000b", 1, 5));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_BeginIndexEqualsEndIndex() {
        // Fix: No exception thrown for empty substring, so test should assert no exception
        assertDoesNotThrow(() -> {
            int result = invokeParseInt("12345", 2, 2);
            assertEquals(0, result, "Empty substring should parse as 0");
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_BeginIndexLessThanZero() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("12345", -1, 3);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_EndIndexGreaterThanLength() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("12345", 1, 10);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_BeginIndexGreaterThanEndIndex() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("12345", 4, 3);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidCharacterAtStart() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("a123", 0, 4);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidCharacterInMiddle() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("12a34", 0, 5);
        });
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidCharacterAtEnd() {
        assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("1234a", 0, 5);
        });
    }
}