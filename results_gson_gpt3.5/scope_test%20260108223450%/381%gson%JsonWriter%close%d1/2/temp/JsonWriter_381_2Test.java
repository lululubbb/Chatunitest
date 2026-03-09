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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterCloseTest {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
        // Initialize stackSize to 1 and stack with EMPTY_DOCUMENT as default context
        setStackAndSize(new int[]{JsonScope.EMPTY_DOCUMENT}, 1);
        // Clear deferredName to avoid interference
        setDeferredName(null);
    }

    @Test
    @Timeout(8000)
    void close_shouldWriteCloseBracketAndDecreaseStackSize_whenContextIsEmpty() throws Throwable {
        // Setup stack and stackSize to simulate EMPTY_ARRAY context
        setStackAndSize(new int[]{JsonScope.EMPTY_ARRAY}, 1);
        setDeferredName(null);

        // Use reflection to invoke private close(int, int, char)
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        JsonWriter result = (JsonWriter) closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');

        assertSame(jsonWriter, result, "close should return this");
        assertEquals(0, getStackSize(), "stackSize should be decremented");
        assertEquals("]", stringWriter.toString(), "Output should contain close bracket");
    }

    @Test
    @Timeout(8000)
    void close_shouldWriteCloseBracketAndDecreaseStackSize_whenContextIsNonEmpty() throws Throwable {
        setStackAndSize(new int[]{JsonScope.NONEMPTY_OBJECT}, 1);
        setDeferredName(null);

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        JsonWriter result = (JsonWriter) closeMethod.invoke(jsonWriter, JsonScope.NONEMPTY_OBJECT, JsonScope.EMPTY_OBJECT, '}');

        assertSame(jsonWriter, result);
        assertEquals(0, getStackSize());
        // Should have newline before close bracket, so output ends with newline + '}'
        String output = stringWriter.toString();
        // The newline() method writes a newline before the close bracket, so output should end with "\n}"
        assertTrue(output.endsWith("\n}") || output.endsWith("}\n"), "Output should contain newline and close bracket");
    }

    @Test
    @Timeout(8000)
    void close_shouldThrowIllegalStateException_whenContextIsInvalid() throws Exception {
        setStackAndSize(new int[]{JsonScope.EMPTY_DOCUMENT}, 1);
        setDeferredName(null);

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        try {
            closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
            fail("Expected IllegalStateException");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IllegalStateException);
            assertEquals("Nesting problem.", cause.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void close_shouldThrowIllegalStateException_whenDeferredNameIsNotNull() throws Exception {
        setStackAndSize(new int[]{JsonScope.EMPTY_OBJECT}, 1);

        // Set deferredName via reflection to simulate dangling name
        setDeferredName("danglingName");

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        try {
            closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
            fail("Expected IllegalStateException");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IllegalStateException);
            assertTrue(cause.getMessage().contains("Dangling name: danglingName"));
        } finally {
            setDeferredName(null);
        }
    }

    private void setStackAndSize(int[] stackValues, int size) throws Exception {
        // set stack field
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        // Copy stackValues into a new array of length 32 (default)
        int[] stackArray = new int[32];
        System.arraycopy(stackValues, 0, stackArray, 0, stackValues.length);
        stackField.set(jsonWriter, stackArray);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, size);
    }

    private int getStackSize() throws Exception {
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        return stackSizeField.getInt(jsonWriter);
    }

    private void setDeferredName(String name) throws Exception {
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, name);
    }
}