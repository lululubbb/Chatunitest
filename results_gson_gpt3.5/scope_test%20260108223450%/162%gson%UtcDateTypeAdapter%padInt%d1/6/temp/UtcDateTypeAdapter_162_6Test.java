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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UtcDateTypeAdapter_162_6Test {

    private Method padIntMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        padIntMethod = UtcDateTypeAdapter.class.getDeclaredMethod("padInt", StringBuilder.class, int.class, int.class);
        padIntMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLengthEqualsLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length = 3, length = 3, no padding expected
        padIntMethod.invoke(null, buffer, 123, 3);
        assertEquals("123", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLengthLessThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length = 2, length = 5, expect 3 zeros padded
        padIntMethod.invoke(null, buffer, 45, 5);
        assertEquals("00045", buffer.toString());
    }

    @Test
    @Timeout(8000)
    public void testPadInt_valueLengthGreaterThanLength() throws InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        // value length = 4, length = 3, no padding, just append value
        padIntMethod.invoke(null, buffer, 1234, 3);
        assertEquals("1234", buffer.toString());
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
        // length = 0, no padding, just append value
        padIntMethod.invoke(null, buffer, 7, 0);
        assertEquals("7", buffer.toString());
    }
}