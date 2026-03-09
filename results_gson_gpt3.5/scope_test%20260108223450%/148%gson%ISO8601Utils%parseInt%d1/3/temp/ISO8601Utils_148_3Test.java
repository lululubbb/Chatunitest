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

public class ISO8601Utils_148_3Test {

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
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ValidInput_SingleDigit() throws Throwable {
        assertEquals(5, invokeParseInt("5", 0, 1));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ValidInput_MultipleDigits() throws Throwable {
        assertEquals(12345, invokeParseInt("12345", 0, 5));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ValidInput_Substring() throws Throwable {
        assertEquals(234, invokeParseInt("a234b", 1, 4));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ValidInput_Zero() throws Throwable {
        assertEquals(0, invokeParseInt("0", 0, 1));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidInput_NegativeBeginIndex() {
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("123", -1, 2);
        });
        assertEquals("123", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidInput_EndIndexGreaterThanLength() {
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("123", 0, 4);
        });
        assertEquals("123", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidInput_BeginIndexGreaterThanEndIndex() {
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("123", 3, 2);
        });
        assertEquals("123", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidInput_NonDigitAtStart() {
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("a123", 0, 4);
        });
        assertEquals("Invalid number: a123", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidInput_NonDigitInMiddle() {
        NumberFormatException ex = assertThrows(NumberFormatException.class, () -> {
            invokeParseInt("12a3", 0, 4);
        });
        assertEquals("Invalid number: 12a3", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ValidInput_LeadingZeros() throws Throwable {
        assertEquals(7, invokeParseInt("007", 0, 3));
    }

    @Test
    @Timeout(8000)
    public void testParseInt_ValidInput_OneDigitSubstring() throws Throwable {
        assertEquals(2, invokeParseInt("x2x", 1, 2));
    }

}