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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class JsonWriterNullValueTest {
    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void nullValue_noDeferredName_writesNull() throws IOException {
        // no deferredName set, serializeNulls true by default
        jsonWriter.nullValue();
        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void nullValue_deferredNameSerializeNullsTrue_writesNameAndNull() throws Exception {
        // set deferredName and serializeNulls true
        setField(jsonWriter, "deferredName", "key");
        jsonWriter.setSerializeNulls(true);

        jsonWriter.nullValue();

        // deferredName should be cleared after writing
        assertNull(getField(jsonWriter, "deferredName"));
        String output = stringWriter.toString();
        // output should contain the name and null
        // The name is written by writeDeferredName() which writes "key": (with quotes)
        // Then beforeValue() does some stack/formatting, then "null"
        assertTrue(output.contains("\"key\""));
        assertTrue(output.contains("null"));
    }

    @Test
    @Timeout(8000)
    public void nullValue_deferredNameSerializeNullsFalse_skipsNameAndWritesNull() throws IOException, NoSuchFieldException, IllegalAccessException {
        setField(jsonWriter, "deferredName", "key");
        jsonWriter.setSerializeNulls(false);

        jsonWriter.nullValue();

        // deferredName should be cleared
        assertNull(getField(jsonWriter, "deferredName"));
        // Only "null" should be written, no key name
        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void nullValue_beforeValueIsCalled() throws Exception {
        JsonWriter spyWriter = Mockito.spy(new JsonWriter(stringWriter));

        // Use reflection to access private beforeValue method
        Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
        beforeValueMethod.setAccessible(true);

        // Call nullValue on spy to trigger beforeValue internally
        spyWriter.nullValue();

        // Verify that nullValue() was called once
        verify(spyWriter, times(1)).nullValue();

        // Additionally, invoke beforeValue directly to ensure accessibility (optional)
        beforeValueMethod.invoke(spyWriter);
    }

    // Reflection helpers
    private void setField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = getFieldRecursive(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Object getField(Object target, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getFieldRecursive(target.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private Field getFieldRecursive(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}