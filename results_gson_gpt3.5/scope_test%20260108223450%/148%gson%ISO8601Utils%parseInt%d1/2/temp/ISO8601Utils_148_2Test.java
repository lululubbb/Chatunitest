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

import org.junit.jupiter.api.Test;

public class ISO8601Utils_148_2Test {

    @Test
    @Timeout(8000)
    public void testParseInt_ValidInput() throws Throwable {
        Method parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);

        // "12345" from index 0 to 5 -> 12345
        int result = (int) parseIntMethod.invoke(null, "12345", 0, 5);
        assertEquals(12345, result);

        // "0" from index 0 to 1 -> 0
        result = (int) parseIntMethod.invoke(null, "0", 0, 1);
        assertEquals(0, result);

        // "007" from index 0 to 3 -> 7
        result = (int) parseIntMethod.invoke(null, "007", 0, 3);
        assertEquals(7, result);

        // "987654321" from index 0 to 9 -> 987654321
        result = (int) parseIntMethod.invoke(null, "987654321", 0, 9);
        assertEquals(987654321, result);

        // substring "234" from "12345" index 1 to 4 -> 234
        result = (int) parseIntMethod.invoke(null, "12345", 1, 4);
        assertEquals(234, result);
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidIndexes() throws Throwable {
        Method parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);

        // beginIndex < 0
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> parseIntMethod.invoke(null, "12345", -1, 3));
        assertTrue(ex.getCause() instanceof NumberFormatException);
        assertEquals("12345", ex.getCause().getMessage());

        // endIndex > length
        ex = assertThrows(InvocationTargetException.class,
                () -> parseIntMethod.invoke(null, "12345", 0, 10));
        assertTrue(ex.getCause() instanceof NumberFormatException);
        assertEquals("12345", ex.getCause().getMessage());

        // beginIndex > endIndex
        ex = assertThrows(InvocationTargetException.class,
                () -> parseIntMethod.invoke(null, "12345", 4, 3));
        assertTrue(ex.getCause() instanceof NumberFormatException);
        assertEquals("12345", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParseInt_InvalidCharacters() throws Throwable {
        Method parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);

        // Non-digit character at start
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> parseIntMethod.invoke(null, "a123", 0, 4));
        assertTrue(ex.getCause() instanceof NumberFormatException);
        assertEquals("Invalid number: a123", ex.getCause().getMessage());

        // Non-digit character in middle
        ex = assertThrows(InvocationTargetException.class,
                () -> parseIntMethod.invoke(null, "12b3", 0, 4));
        assertTrue(ex.getCause() instanceof NumberFormatException);
        assertEquals("Invalid number: 12b3", ex.getCause().getMessage());

        // Non-digit character at end
        ex = assertThrows(InvocationTargetException.class,
                () -> parseIntMethod.invoke(null, "123c", 0, 4));
        assertTrue(ex.getCause() instanceof NumberFormatException);
        assertEquals("Invalid number: 123c", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParseInt_EmptySubstring() throws Throwable {
        Method parseIntMethod = ISO8601Utils.class.getDeclaredMethod("parseInt", String.class, int.class, int.class);
        parseIntMethod.setAccessible(true);

        // beginIndex == endIndex, empty substring -> should NOT throw exception, returns 0
        int result = (int) parseIntMethod.invoke(null, "12345", 2, 2);
        assertEquals(0, result);
    }
}