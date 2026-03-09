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

class ISO8601Utils_150_4Test {

    private static Method indexOfNonDigitMethod;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        indexOfNonDigitMethod = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigitMethod.setAccessible(true);
    }

    private int invokeIndexOfNonDigit(String string, int offset) {
        try {
            return (int) indexOfNonDigitMethod.invoke(null, string, offset);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testIndexOfNonDigit_AllDigitsFromOffset() {
        String input = "1234567890";
        int offset = 0;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(input.length(), result);
    }

    @Test
    @Timeout(8000)
    void testIndexOfNonDigit_NonDigitAtStart() {
        String input = "a1234567890";
        int offset = 0;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void testIndexOfNonDigit_NonDigitInMiddle() {
        String input = "12345a67890";
        int offset = 0;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(5, result);
    }

    @Test
    @Timeout(8000)
    void testIndexOfNonDigit_NonDigitAfterOffset() {
        String input = "1234567890a";
        int offset = 5;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(10, result);
    }

    @Test
    @Timeout(8000)
    void testIndexOfNonDigit_OffsetBeyondNonDigit() {
        String input = "12345a67890";
        int offset = 6;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(input.length(), result);
    }

    @Test
    @Timeout(8000)
    void testIndexOfNonDigit_EmptyString() {
        String input = "";
        int offset = 0;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void testIndexOfNonDigit_OffsetAtStringLength() {
        String input = "12345";
        int offset = input.length();
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(input.length(), result);
    }
}