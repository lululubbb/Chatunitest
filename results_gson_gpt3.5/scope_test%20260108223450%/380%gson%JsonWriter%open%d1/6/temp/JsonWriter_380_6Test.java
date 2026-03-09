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
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.google.gson.stream.JsonWriter;

public class JsonWriter_380_6Test {

    private Writer out;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        out = mock(Writer.class);
        jsonWriter = new JsonWriter(out);
    }

    @Test
    @Timeout(8000)
    public void testOpen_emptyArray() throws Throwable {
        invokeOpen(JsonScope.EMPTY_ARRAY, '[');

        verify(out).write('[');
        assertStackTop(JsonScope.EMPTY_ARRAY);
    }

    @Test
    @Timeout(8000)
    public void testOpen_emptyObject() throws Throwable {
        invokeOpen(JsonScope.EMPTY_OBJECT, '{');

        verify(out).write('{');
        assertStackTop(JsonScope.EMPTY_OBJECT);
    }

    @Test
    @Timeout(8000)
    public void testOpen_nonemptyArray() throws Throwable {
        invokeOpen(JsonScope.NONEMPTY_ARRAY, '[');

        verify(out).write('[');
        assertStackTop(JsonScope.NONEMPTY_ARRAY);
    }

    @Test
    @Timeout(8000)
    public void testOpen_nonemptyObject() throws Throwable {
        invokeOpen(JsonScope.NONEMPTY_OBJECT, '{');

        verify(out).write('{');
        assertStackTop(JsonScope.NONEMPTY_OBJECT);
    }

    @Test
    @Timeout(8000)
    public void testOpen_danglingName() throws Throwable {
        // Setup stack with DANGLING_NAME on top to trigger beforeValue branch
        pushStack(JsonScope.DANGLING_NAME);

        invokeOpen(JsonScope.EMPTY_ARRAY, '[');

        verify(out).write('[');
        assertStackTop(JsonScope.EMPTY_ARRAY);
    }

    // Helper to invoke private open method via reflection
    private JsonWriter invokeOpen(int empty, char openBracket) throws Throwable {
        Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
        openMethod.setAccessible(true);
        try {
            return (JsonWriter) openMethod.invoke(jsonWriter, empty, openBracket);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    // Helper to assert stack top value
    private void assertStackTop(int expectedTop) throws Throwable {
        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        int top = (int) peekMethod.invoke(jsonWriter);
        assertEquals(expectedTop, top);
    }

    // Helper to push a value to stack using private push method
    private void pushStack(int value) throws Throwable {
        Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(jsonWriter, value);
    }
}