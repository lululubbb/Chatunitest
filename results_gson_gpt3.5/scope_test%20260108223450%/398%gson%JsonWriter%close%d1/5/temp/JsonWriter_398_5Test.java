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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_398_5Test {

    private Writer mockWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        mockWriter = mock(Writer.class);
        jsonWriter = new JsonWriter(mockWriter);
    }

    @Test
    @Timeout(8000)
    public void close_whenStackEmpty_closesOutAndResetsStackSize() throws Exception {
        // stackSize = 0, no exception expected
        setField(jsonWriter, "stackSize", 0);

        jsonWriter.close();

        verify(mockWriter).close();
        assertEquals(0, ((Integer) getField(jsonWriter, "stackSize")).intValue());
    }

    @Test
    @Timeout(8000)
    public void close_whenStackSizeIsOneAndTopIsNonemptyDocument_closesOutAndResetsStackSize() throws Exception {
        setField(jsonWriter, "stackSize", 1);
        int[] stack = new int[32];
        stack[0] = JsonScope.NONEMPTY_DOCUMENT;
        setField(jsonWriter, "stack", stack);

        jsonWriter.close();

        verify(mockWriter).close();
        assertEquals(0, ((Integer) getField(jsonWriter, "stackSize")).intValue());
    }

    @Test
    @Timeout(8000)
    public void close_whenStackSizeIsOneAndTopIsNotNonemptyDocument_throwsIOException() throws Exception {
        setField(jsonWriter, "stackSize", 1);
        int[] stack = new int[32];
        stack[0] = JsonScope.EMPTY_DOCUMENT; // different from NONEMPTY_DOCUMENT
        setField(jsonWriter, "stack", stack);

        IOException thrown = assertThrows(IOException.class, () -> jsonWriter.close());
        assertEquals("Incomplete document", thrown.getMessage());
        verify(mockWriter).close();
    }

    @Test
    @Timeout(8000)
    public void close_whenStackSizeGreaterThanOne_throwsIOException() throws Exception {
        setField(jsonWriter, "stackSize", 2);
        int[] stack = new int[32];
        stack[0] = JsonScope.EMPTY_DOCUMENT;
        stack[1] = JsonScope.EMPTY_ARRAY;
        setField(jsonWriter, "stack", stack);

        IOException thrown = assertThrows(IOException.class, () -> jsonWriter.close());
        assertEquals("Incomplete document", thrown.getMessage());
        verify(mockWriter).close();
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonWriter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Object getField(Object target, String fieldName) throws Exception {
        Field field = JsonWriter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}