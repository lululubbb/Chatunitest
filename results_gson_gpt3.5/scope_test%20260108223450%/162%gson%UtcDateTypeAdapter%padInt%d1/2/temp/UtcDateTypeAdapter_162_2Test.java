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

public class UtcDateTypeAdapter_162_2Test {
    private static Method padIntMethod;

    @BeforeAll
    public static void setUp() throws NoSuchMethodException {
        padIntMethod = UtcDateTypeAdapter.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPadInt_exactLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        // value length == length
        padIntMethod.invoke(null, sb, 123, 3);
        assertEquals("123", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_lessLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        // value length < length, should pad zeros
        padIntMethod.invoke(null, sb, 7, 3);
        assertEquals("007", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_zeroValue() throws InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        // value = 0, length > 1, should pad zeros
        padIntMethod.invoke(null, sb, 0, 4);
        assertEquals("0000", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_lengthLessThanValueLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        // length < value length, no padding, just append value string
        padIntMethod.invoke(null, sb, 12345, 3);
        assertEquals("12345", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_lengthZero() throws InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        // length = 0, no padding, just append value string
        padIntMethod.invoke(null, sb, 42, 0);
        assertEquals("42", sb.toString());
    }
}