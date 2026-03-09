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
import com.google.gson.stream.JsonScope;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonWriter_379_4Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_whenStackTopIsEmptyObject_shouldCloseObject() throws IOException {
        // Arrange: simulate stack with EMPTY_OBJECT on top
        pushStack(jsonWriter, JsonScope.EMPTY_OBJECT);

        // Act
        JsonWriter returned = jsonWriter.endObject();

        // Assert
        assertSame(jsonWriter, returned);
        // The stack top should be popped or replaced by NONEMPTY_OBJECT (internal behavior)
        int top = peekStack(jsonWriter);
        assertNotEquals(JsonScope.EMPTY_OBJECT, top);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_whenStackTopIsNonEmptyObject_shouldCloseObject() throws IOException {
        // Arrange: simulate stack with NONEMPTY_OBJECT on top
        pushStack(jsonWriter, JsonScope.NONEMPTY_OBJECT);

        // Act
        JsonWriter returned = jsonWriter.endObject();

        // Assert
        assertSame(jsonWriter, returned);
        int top = peekStack(jsonWriter);
        assertNotEquals(JsonScope.NONEMPTY_OBJECT, top);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_whenStackTopIsInvalid_shouldThrowException() {
        // Arrange: simulate stack with invalid top (e.g. EMPTY_ARRAY)
        pushStack(jsonWriter, JsonScope.EMPTY_ARRAY);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> jsonWriter.endObject());
        assertTrue(exception.getMessage().contains("Nesting problem"));
    }

    // Utility method to push int on stack using reflection
    private void pushStack(JsonWriter writer, int value) {
        try {
            Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
            pushMethod.setAccessible(true);
            pushMethod.invoke(writer, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Failed to invoke push method via reflection: " + e.getMessage());
        }
    }

    // Utility method to peek stack top using reflection
    private int peekStack(JsonWriter writer) {
        try {
            Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
            peekMethod.setAccessible(true);
            return (int) peekMethod.invoke(writer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Failed to invoke peek method via reflection: " + e.getMessage());
            return -1;
        }
    }
}