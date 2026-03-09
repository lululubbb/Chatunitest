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

public class ISO8601Utils_150_1Test {

    private static Method indexOfNonDigitMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        indexOfNonDigitMethod = ISO8601Utils.class.getDeclaredMethod("indexOfNonDigit", String.class, int.class);
        indexOfNonDigitMethod.setAccessible(true);
    }

    private int invokeIndexOfNonDigit(String string, int offset) throws InvocationTargetException, IllegalAccessException {
        return (int) indexOfNonDigitMethod.invoke(null, string, offset);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_AllDigitsFromOffset() throws Exception {
        String input = "1234567890";
        int offset = 0;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(input.length(), result);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_NonDigitAtStart() throws Exception {
        String input = "a1234567890";
        int offset = 0;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_NonDigitInMiddle() throws Exception {
        String input = "1234a567890";
        int offset = 0;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(4, result);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_OffsetInsideDigits() throws Exception {
        String input = "1234567890";
        int offset = 5;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(input.length(), result);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_OffsetAtNonDigit() throws Exception {
        String input = "1234a567890";
        int offset = 4;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(4, result);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_OffsetAfterNonDigit() throws Exception {
        String input = "1234a567890";
        int offset = 5;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(input.length(), result);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_EmptyString() throws Exception {
        String input = "";
        int offset = 0;
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_OffsetEqualsLength() throws Exception {
        String input = "12345";
        int offset = input.length();
        int result = invokeIndexOfNonDigit(input, offset);
        assertEquals(input.length(), result);
    }
}