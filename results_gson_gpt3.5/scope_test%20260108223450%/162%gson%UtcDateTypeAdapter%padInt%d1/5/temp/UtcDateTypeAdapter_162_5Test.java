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

public class UtcDateTypeAdapter_162_5Test {

    private static Method padIntMethod;

    @BeforeAll
    public static void setup() throws NoSuchMethodException {
        padIntMethod = UtcDateTypeAdapter.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLengthEqualsLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length == length, no padding expected
        padIntMethod.invoke(null, buffer, 123, 3);
        assertEquals("123", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLengthLessThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length < length, padding expected
        padIntMethod.invoke(null, buffer, 7, 3);
        assertEquals("007", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLengthGreaterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length > length, no padding, just append value string
        padIntMethod.invoke(null, buffer, 12345, 3);
        assertEquals("12345", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_zeroLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder("prefix");
        // length == 0, no padding, just append value string
        padIntMethod.invoke(null, buffer, 42, 0);
        assertEquals("prefix42", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_zeroValue() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value = 0, length = 3, expect "000"
        padIntMethod.invoke(null, buffer, 0, 3);
        assertEquals("000", buffer.toString());
    }
}