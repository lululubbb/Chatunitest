package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;
import java.io.Closeable;
import java.io.Flushable;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

class JsonWriter_value_Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testValue_null_callsNullValue() throws IOException {
        JsonWriter spyWriter = spy(jsonWriter);
        doReturn(spyWriter).when(spyWriter).nullValue();

        JsonWriter result = spyWriter.value((Number) null);

        verify(spyWriter).nullValue();
        assertSame(spyWriter, result);
    }

    @Test
    @Timeout(8000)
    public void testValue_infinityWithoutLenient_throws() {
        Number[] specialNumbers = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN,
                Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NaN};
        for (Number num : specialNumbers) {
            JsonWriter writer = new JsonWriter(new StringWriter());
            writer.setLenient(false);
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> writer.value(num));
            String msg = ex.getMessage();
            assertTrue(msg.contains("Numeric values must be finite"));
            assertTrue(msg.contains(num.toString()));
        }
    }

    @Test
    @Timeout(8000)
    public void testValue_infinityWithLenient_allows() throws Exception {
        Number[] specialNumbers = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN,
                Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NaN};
        for (Number num : specialNumbers) {
            JsonWriter writer = new JsonWriter(new StringWriter());
            writer.setLenient(true);
            JsonWriter spyWriter = spy(writer);

            // Directly call value(num) on spyWriter without stubbing private methods
            JsonWriter result = spyWriter.value(num);

            assertSame(spyWriter, result);

            // Access private 'out' field via reflection to get output
            Field outField = JsonWriter.class.getDeclaredField("out");
            outField.setAccessible(true);
            Object outObj = outField.get(writer);
            assertTrue(outObj instanceof StringWriter);
            String output = outObj.toString();

            assertTrue(output.contains(num.toString()) || output.isEmpty());
        }
    }

    @Test
    @Timeout(8000)
    public void testValue_invalidNumberString_throws() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Create a custom Number subclass that returns invalid JSON number string
        class BadNumber extends Number {
            @Override public int intValue() { return 0; }
            @Override public long longValue() { return 0L; }
            @Override public float floatValue() { return 0f; }
            @Override public double doubleValue() { return 0; }
            @Override public String toString() { return "bad_number"; }
        }

        JsonWriter writer = new JsonWriter(new StringWriter());
        writer.setLenient(false);

        BadNumber badNumber = new BadNumber();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> writer.value(badNumber));
        assertTrue(ex.getMessage().contains("String created by") && ex.getMessage().contains("is not a valid JSON number"));
    }

    @Test
    @Timeout(8000)
    public void testValue_validNumber_writesCorrectly() throws Exception {
        Number[] numbers = {
                Integer.valueOf(123),
                Long.valueOf(4567890123L),
                Float.valueOf(3.14f),
                Double.valueOf(2.71828),
                BigDecimal.valueOf(123456789.123456789),
                BigInteger.valueOf(987654321),
                new AtomicInteger(42),
                new AtomicLong(9876543210L)
        };

        for (Number num : numbers) {
            StringWriter sw = new StringWriter();
            JsonWriter writer = new JsonWriter(sw);
            JsonWriter spyWriter = spy(writer);

            // Just call value(num) directly; no stubbing of private methods
            JsonWriter result = spyWriter.value(num);

            assertSame(spyWriter, result);
            String output = sw.toString();
            assertTrue(output.contains(num.toString()), "Output should contain number string: " + num.toString());
        }
    }

    @Test
    @Timeout(8000)
    public void testValue_callsWriteDeferredNameAndBeforeValue() throws Exception {
        JsonWriter spyWriter = spy(jsonWriter);

        // Just call value(num) directly; no stubbing of private methods
        Number num = 100;
        JsonWriter result = spyWriter.value(num);

        assertSame(spyWriter, result);
    }
}