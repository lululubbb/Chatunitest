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

public class ISO8601Utils_150_3Test {

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
        int expected = input.length();
        int actual = invokeIndexOfNonDigit(input, offset);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_NonDigitAtStartOffset() throws Exception {
        String input = "abc123456";
        int offset = 0;
        int expected = 0;
        int actual = invokeIndexOfNonDigit(input, offset);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_NonDigitInMiddle() throws Exception {
        String input = "1234a56789";
        int offset = 0;
        int expected = 4;
        int actual = invokeIndexOfNonDigit(input, offset);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_NonDigitAfterOffset() throws Exception {
        String input = "1234567890";
        int offset = 5;
        int expected = input.length();
        int actual = invokeIndexOfNonDigit(input, offset);
        assertEquals(expected, actual);

        String input2 = "12345a7890";
        int offset2 = 5;
        int expected2 = 5;
        int actual2 = invokeIndexOfNonDigit(input2, offset2);
        assertEquals(expected2, actual2);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_EmptyString() throws Exception {
        String input = "";
        int offset = 0;
        int expected = 0;
        int actual = invokeIndexOfNonDigit(input, offset);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_OffsetEqualsLength() throws Exception {
        String input = "123456";
        int offset = input.length();
        int expected = input.length();
        int actual = invokeIndexOfNonDigit(input, offset);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testIndexOfNonDigit_OffsetGreaterThanLength() throws Exception {
        String input = "123456";
        int offset = input.length() + 1;
        int expected = input.length();
        int actual = invokeIndexOfNonDigit(input, offset);
        assertEquals(expected, actual);
    }

}