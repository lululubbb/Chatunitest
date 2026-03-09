package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonWriterBeforeValueTest {

    private JsonWriter writer;
    private Writer mockOut;

    @BeforeEach
    void setUp() {
        mockOut = Mockito.mock(Writer.class);
        writer = new JsonWriter(mockOut);
    }

    private void setStackAndStackSize(int[] stackValues, int stackSize) throws Exception {
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(writer, stackValues);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(writer, stackSize);
    }

    private void setLenient(boolean lenient) throws Exception {
        Field lenientField = JsonWriter.class.getDeclaredField("lenient");
        lenientField.setAccessible(true);
        lenientField.setBoolean(writer, lenient);
    }

    private void setSeparator(String separator) throws Exception {
        Field separatorField = JsonWriter.class.getDeclaredField("separator");
        separatorField.setAccessible(true);
        separatorField.set(writer, separator);
    }

    private int peek() throws Exception {
        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        return (int) peekMethod.invoke(writer);
    }

    private void replaceTop(int value) throws Exception {
        Method replaceTopMethod = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
        replaceTopMethod.setAccessible(true);
        replaceTopMethod.invoke(writer, value);
    }

    private void invokeNewline(Object target) throws Exception {
        Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
        newlineMethod.setAccessible(true);
        newlineMethod.invoke(target);
    }

    private void beforeValue() throws Throwable {
        Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
        beforeValueMethod.setAccessible(true);
        try {
            beforeValueMethod.invoke(writer);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testBeforeValue_nonEmptyDocumentLenientFalse_throws() throws Throwable {
        // Setup stack so peek returns NONEMPTY_DOCUMENT
        int[] stack = new int[32];
        stack[0] = NONEMPTY_DOCUMENT;
        setStackAndStackSize(stack, 1);
        setLenient(false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, this::beforeValue);
        assertEquals("JSON must have only one top-level value.", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testBeforeValue_nonEmptyDocumentLenientTrue_replacesTop() throws Throwable {
        int[] stack = new int[32];
        stack[0] = NONEMPTY_DOCUMENT;
        setStackAndStackSize(stack, 1);
        setLenient(true);

        beforeValue();

        // Verify top replaced with NONEMPTY_DOCUMENT (same value)
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] currentStack = (int[]) stackField.get(writer);
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(writer);

        assertEquals(1, stackSize);
        assertEquals(NONEMPTY_DOCUMENT, currentStack[0]);
    }

    @Test
    @Timeout(8000)
    void testBeforeValue_emptyDocument_replacesTop() throws Throwable {
        int[] stack = new int[32];
        stack[0] = EMPTY_DOCUMENT;
        setStackAndStackSize(stack, 1);

        beforeValue();

        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] currentStack = (int[]) stackField.get(writer);
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(writer);

        assertEquals(1, stackSize);
        assertEquals(NONEMPTY_DOCUMENT, currentStack[0]);
    }

    @Test
    @Timeout(8000)
    void testBeforeValue_emptyArray_replacesTopAndNewline() throws Throwable {
        int[] stack = new int[32];
        stack[0] = EMPTY_ARRAY;
        setStackAndStackSize(stack, 1);

        // Spy on writer to verify newline call
        JsonWriter spyWriter = Mockito.spy(writer);

        // Set spy's stack and stackSize fields same as original writer
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(spyWriter, stack);
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(spyWriter, 1);

        // Use reflection to invoke beforeValue on spyWriter
        Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
        beforeValueMethod.setAccessible(true);
        beforeValueMethod.invoke(spyWriter);

        int[] currentStack = (int[]) stackField.get(spyWriter);
        int stackSize = stackSizeField.getInt(spyWriter);
        assertEquals(1, stackSize);
        assertEquals(NONEMPTY_ARRAY, currentStack[0]);

        // Verify that newline() was called once via reflection
        Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
        newlineMethod.setAccessible(true);
        verify(spyWriter, times(1)).invoke(newlineMethod);
    }

    @Test
    @Timeout(8000)
    void testBeforeValue_nonEmptyArray_appendsCommaAndNewline() throws Throwable {
        int[] stack = new int[32];
        stack[0] = NONEMPTY_ARRAY;
        setStackAndStackSize(stack, 1);

        // Spy on writer to verify newline call and out.append(',')
        JsonWriter spyWriter = Mockito.spy(writer);

        // Set spy's stack and stackSize fields same as original writer
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(spyWriter, stack);
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(spyWriter, 1);

        // Use reflection to invoke beforeValue on spyWriter
        Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
        beforeValueMethod.setAccessible(true);
        beforeValueMethod.invoke(spyWriter);

        // Verify out.append(',') called
        verify(mockOut).append(',');

        // Verify newline was called once via reflection
        Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
        newlineMethod.setAccessible(true);
        verify(spyWriter, times(1)).invoke(newlineMethod);
    }

    @Test
    @Timeout(8000)
    void testBeforeValue_danglingName_appendsSeparatorAndReplacesTop() throws Throwable {
        int[] stack = new int[32];
        stack[0] = DANGLING_NAME;
        setStackAndStackSize(stack, 1);
        setSeparator(": ");

        beforeValue();

        verify(mockOut).append(": ");

        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] currentStack = (int[]) stackField.get(writer);
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = stackSizeField.getInt(writer);

        assertEquals(1, stackSize);
        assertEquals(NONEMPTY_OBJECT, currentStack[0]);
    }

    @Test
    @Timeout(8000)
    void testBeforeValue_invalidPeek_throws() throws Throwable {
        int[] stack = new int[32];
        stack[0] = 9999; // invalid state
        setStackAndStackSize(stack, 1);

        IllegalStateException ex = assertThrows(IllegalStateException.class, this::beforeValue);
        assertEquals("Nesting problem.", ex.getMessage());
    }
}