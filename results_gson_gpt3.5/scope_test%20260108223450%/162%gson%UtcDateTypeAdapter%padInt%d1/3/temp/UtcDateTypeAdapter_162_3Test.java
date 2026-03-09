package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_162_3Test {

    private static Method padIntMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        padIntMethod = UtcDateTypeAdapter.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLengthLessThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value = 42 (length 2), length = 5, expect 3 zeros + "42"
        padIntMethod.invoke(null, buffer, 42, 5);
        assertEquals("00042", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLengthEqualsLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value = 12345 (length 5), length = 5, expect "12345" only
        padIntMethod.invoke(null, buffer, 12345, 5);
        assertEquals("12345", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLengthGreaterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value = 123456 (length 6), length = 5, expect "123456" only (no truncation)
        padIntMethod.invoke(null, buffer, 123456, 5);
        assertEquals("123456", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_zeroValue() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value = 0, length = 3, expect "000"
        padIntMethod.invoke(null, buffer, 0, 3);
        assertEquals("000", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_lengthZero() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value = 7, length = 0, expect "7" (length param less than value length, no zeros)
        padIntMethod.invoke(null, buffer, 7, 0);
        assertEquals("7", buffer.toString());
    }
}