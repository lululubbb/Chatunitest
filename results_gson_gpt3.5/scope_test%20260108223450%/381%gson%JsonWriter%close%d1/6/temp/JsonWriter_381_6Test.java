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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.stream.JsonWriter;

class JsonWriterCloseTest {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
        // Initialize stackSize and stack properly to avoid IllegalStateException on peek()
        try {
            setField(jsonWriter, "stackSize", 1);
            int[] stack = new int[32];
            stack[0] = 1; // EMPTY_DOCUMENT or EMPTY_OBJECT context for valid peek()
            setField(jsonWriter, "stack", stack);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void close_shouldThrowIllegalStateException_whenContextNotEmptyOrNonempty() throws Exception {
        // Use reflection to set stackSize and stack to simulate invalid context
        setField(jsonWriter, "stackSize", 1);
        int[] stack = new int[32];
        stack[0] = 999; // invalid context
        setField(jsonWriter, "stack", stack);
        // deferredName must be null
        setField(jsonWriter, "deferredName", null);

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> closeMethod.invoke(jsonWriter, 1, 2, ']'));
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertTrue(cause.getMessage().contains("Nesting problem."));
    }

    @Test
    @Timeout(8000)
    public void close_shouldThrowIllegalStateException_whenDeferredNameNotNull() throws Exception {
        setField(jsonWriter, "stackSize", 1);
        int[] stack = new int[32];
        stack[0] = 1; // valid context matching 'empty' param
        setField(jsonWriter, "stack", stack);
        setField(jsonWriter, "deferredName", "name");

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> closeMethod.invoke(jsonWriter, 1, 1, '}'));
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertTrue(cause.getMessage().contains("Dangling name: name"));
    }

    @Test
    @Timeout(8000)
    public void close_shouldDecrementStackSizeAndWriteCloseBracket_whenContextIsNonempty() throws Exception {
        setField(jsonWriter, "stackSize", 1);
        int[] stack = new int[32];
        stack[0] = 2; // nonempty context
        setField(jsonWriter, "stack", stack);
        setField(jsonWriter, "deferredName", null);

        // Spy the jsonWriter instance
        JsonWriter spyWriter = spy(jsonWriter);

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        JsonWriter returned = (JsonWriter) closeMethod.invoke(spyWriter, 1, 2, ']');

        assertEquals(0, getField(spyWriter, "stackSize"));
        String output = stringWriter.toString();
        assertFalse(output.isEmpty());
        assertEquals(']', output.charAt(output.length() - 1));
        assertSame(spyWriter, returned);
    }

    @Test
    @Timeout(8000)
    public void close_shouldDecrementStackSizeAndWriteCloseBracket_whenContextIsEmpty() throws Exception {
        setField(jsonWriter, "stackSize", 1);
        int[] stack = new int[32];
        stack[0] = 1; // empty context
        setField(jsonWriter, "stack", stack);
        setField(jsonWriter, "deferredName", null);

        JsonWriter spyWriter = spy(jsonWriter);

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        JsonWriter returned = (JsonWriter) closeMethod.invoke(spyWriter, 1, 1, '}');

        assertEquals(0, getField(spyWriter, "stackSize"));
        String output = stringWriter.toString();
        assertFalse(output.isEmpty());
        assertEquals('}', output.charAt(output.length() - 1));
        assertSame(spyWriter, returned);
    }

    // Helper method to set private fields via reflection
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // Helper method to get private fields via reflection
    private static Object getField(Object target, String fieldName) throws Exception {
        Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private static Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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