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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_377_5Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void endArray_afterBeginArray_shouldCloseArray() throws IOException {
        jsonWriter.beginArray();
        JsonWriter returned = jsonWriter.endArray();
        assertSame(jsonWriter, returned);
        assertEquals("[]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void endArray_afterBeginArrayWithValue_shouldCloseArray() throws IOException {
        jsonWriter.beginArray();
        jsonWriter.value("value");
        JsonWriter returned = jsonWriter.endArray();
        assertSame(jsonWriter, returned);
        assertEquals("[\"value\"]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void endArray_withoutBeginArray_shouldThrowIllegalStateException() {
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            jsonWriter.endArray();
        });
        assertTrue(thrown.getMessage().contains("Nesting problem"));
    }

    @Test
    @Timeout(8000)
    void closeMethod_invokedByEndArray_shouldBehaveCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // Use reflection to invoke private close(int, int, char) method directly to check behavior
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        jsonWriter.beginArray();
        Object returned = closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
        assertSame(jsonWriter, returned);
        assertEquals("[]", stringWriter.toString());
    }
}