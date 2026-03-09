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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.stream.JsonWriter;

class JsonWriter_value_Test {

    private Writer mockWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        mockWriter = mock(Writer.class);
        jsonWriter = new JsonWriter(mockWriter);
    }

    @Test
    @Timeout(8000)
    public void value_writesFloatValueSuccessfully() throws Exception {
        // Arrange
        setLenient(false);
        setDeferredName(null);
        pushStack(EMPTY_DOCUMENT);

        // Act
        JsonWriter returned = jsonWriter.value(123.45f);

        // Assert
        assertSame(jsonWriter, returned);
        verify(mockWriter).append("123.45");
    }

    @Test
    @Timeout(8000)
    public void value_allowsNaN_andInfinite_whenLenient() throws Exception {
        setLenient(true);
        setDeferredName(null);
        pushStack(EMPTY_DOCUMENT);

        JsonWriter returnedNaN = jsonWriter.value(Float.NaN);
        verify(mockWriter).append("NaN");
        assertSame(jsonWriter, returnedNaN);

        JsonWriter returnedPosInf = jsonWriter.value(Float.POSITIVE_INFINITY);
        verify(mockWriter).append("Infinity");
        assertSame(jsonWriter, returnedPosInf);

        JsonWriter returnedNegInf = jsonWriter.value(Float.NEGATIVE_INFINITY);
        verify(mockWriter).append("-Infinity");
        assertSame(jsonWriter, returnedNegInf);
    }

    @Test
    @Timeout(8000)
    public void value_throwsIllegalArgumentException_forNaN_whenNotLenient() throws Exception {
        setLenient(false);
        setDeferredName(null);
        pushStack(EMPTY_DOCUMENT);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Float.NaN));
        assertTrue(ex.getMessage().contains("Numeric values must be finite"));

        ex = assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Float.POSITIVE_INFINITY));
        assertTrue(ex.getMessage().contains("Numeric values must be finite"));

        ex = assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Float.NEGATIVE_INFINITY));
        assertTrue(ex.getMessage().contains("Numeric values must be finite"));
    }

    @Test
    @Timeout(8000)
    public void value_callsWriteDeferredName_andBeforeValue() throws Exception {
        setLenient(false);
        // Create spy on jsonWriter
        JsonWriter spyWriter = spy(jsonWriter);

        // Set deferredName to "name" on spyWriter
        setField(spyWriter, "deferredName", "name");

        // Replace 'out' with spy on mockWriter
        Writer spyOut = spy(mockWriter);
        setField(spyWriter, "out", spyOut);

        // Push EMPTY_DOCUMENT state on spyWriter's stack
        pushStack(spyWriter, EMPTY_DOCUMENT);

        // Use reflection to get public value(float) method
        Method valueMethod = JsonWriter.class.getMethod("value", float.class);
        // invoke value(float) on spyWriter
        valueMethod.invoke(spyWriter, 42.0f);

        // Assert deferredName cleared
        assertNull(getField(spyWriter, "deferredName"));

        // Assert stack top is NONEMPTY_DOCUMENT
        int[] stack = (int[]) getField(spyWriter, "stack");
        int stackSize = (int) getField(spyWriter, "stackSize");
        int top = stack[stackSize - 1];
        assertEquals(NONEMPTY_DOCUMENT, top);

        // Verify append called with "42.0"
        verify(spyOut).append("42.0");
    }

    // Helper methods to access private fields and manipulate state

    private static final int EMPTY_DOCUMENT = 6;
    private static final int NONEMPTY_DOCUMENT = 7;

    private void setLenient(boolean lenient) throws Exception {
        setField(jsonWriter, "lenient", lenient);
    }

    private void setDeferredName(String name) throws Exception {
        setField(jsonWriter, "deferredName", name);
    }

    private void pushStack(int state) throws Exception {
        int[] stack = (int[]) getField(jsonWriter, "stack");
        int stackSize = (int) getField(jsonWriter, "stackSize");
        if (stackSize == stack.length) {
            int[] newStack = new int[stackSize * 2];
            System.arraycopy(stack, 0, newStack, 0, stackSize);
            setField(jsonWriter, "stack", newStack);
            stack = newStack;
        }
        stack[stackSize] = state;
        setField(jsonWriter, "stackSize", stackSize + 1);
    }

    private void pushStack(JsonWriter writer, int state) throws Exception {
        int[] stack = (int[]) getField(writer, "stack");
        int stackSize = (int) getField(writer, "stackSize");
        if (stackSize == stack.length) {
            int[] newStack = new int[stackSize * 2];
            System.arraycopy(stack, 0, newStack, 0, stackSize);
            setField(writer, "stack", newStack);
            stack = newStack;
        }
        stack[stackSize] = state;
        setField(writer, "stackSize", stackSize + 1);
    }

    private static void setField(Object obj, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = getFieldFromClassHierarchy(obj.getClass(), fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    private static Object getField(Object obj, String fieldName) throws Exception {
        java.lang.reflect.Field field = getFieldFromClassHierarchy(obj.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private static java.lang.reflect.Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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