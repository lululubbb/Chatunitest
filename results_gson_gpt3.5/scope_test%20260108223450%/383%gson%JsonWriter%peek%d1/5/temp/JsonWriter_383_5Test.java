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

public class JsonWriter_383_5Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void peek_whenStackSizeIsZero_throwsIllegalStateException() throws Exception {
        // Use reflection to set stackSize to 0
        setField(jsonWriter, "stackSize", 0);

        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            peekMethod.invoke(jsonWriter);
        });
        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalStateException);
        assertEquals("JsonWriter is closed.", cause.getMessage());
    }

    @Test
    @Timeout(8000)
    public void peek_returnsTopOfStack() throws Exception {
        int[] stack = new int[32];
        stack[0] = 42;
        setField(jsonWriter, "stack", stack);
        setField(jsonWriter, "stackSize", 1);

        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        int result = (int) peekMethod.invoke(jsonWriter);
        assertEquals(42, result);
    }

    @Test
    @Timeout(8000)
    public void peek_returnsTopOfStack_multipleElements() throws Exception {
        int[] stack = new int[32];
        stack[0] = 10;
        stack[1] = 20;
        stack[2] = 30;
        setField(jsonWriter, "stack", stack);
        setField(jsonWriter, "stackSize", 3);

        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        int result = (int) peekMethod.invoke(jsonWriter);
        assertEquals(30, result);
    }

    // Helper method to set private fields via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = JsonWriter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}