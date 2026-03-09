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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_377_4Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void endArray_shouldCloseEmptyArray() throws IOException {
        jsonWriter.beginArray();
        JsonWriter returned = jsonWriter.endArray();
        assertSame(jsonWriter, returned);
        assertEquals("[]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void endArray_shouldCloseNonEmptyArray() throws IOException {
        jsonWriter.beginArray();
        jsonWriter.value("a");
        JsonWriter returned = jsonWriter.endArray();
        assertSame(jsonWriter, returned);
        assertEquals("[\"a\"]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void endArray_shouldThrow_whenStackEmpty() {
        assertThrows(IllegalStateException.class, () -> {
            jsonWriter.endArray();
        });
    }

    @Test
    @Timeout(8000)
    void close_method_invoked_by_endArray_correctlyReplacesStack() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to call private close method with EMPTY_ARRAY, NONEMPTY_ARRAY and ']'
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        jsonWriter.beginArray();
        // close should close the array and write ']'
        JsonWriter returned = (JsonWriter) closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
        assertSame(jsonWriter, returned);
        assertEquals("[]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void close_method_shouldThrow_whenStackTopMismatch() throws IOException, NoSuchMethodException {
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        jsonWriter.beginObject();
        // Trying to close array when top is object should throw
        Throwable thrown = assertThrows(InvocationTargetException.class, () -> {
            closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
        });
        assertTrue(thrown.getCause() instanceof IllegalStateException);
    }
}