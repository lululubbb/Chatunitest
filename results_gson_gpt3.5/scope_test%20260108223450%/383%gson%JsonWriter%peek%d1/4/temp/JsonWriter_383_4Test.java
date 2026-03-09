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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonWriterPeekTest {

    private JsonWriter jsonWriter;
    private Method peekMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException, NoSuchFieldException {
        jsonWriter = new JsonWriter(new StringWriter());
        peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        try {
            setField(jsonWriter, "stackSize", 1);
            int[] stack = new int[32];
            stack[0] = 6; // EMPTY_DOCUMENT constant as per JsonScope.EMPTY_DOCUMENT
            setField(jsonWriter, "stack", stack);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void peek_stackSizeZero_throwsIllegalStateException() throws Exception {
        setField(jsonWriter, "stackSize", 0);

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
    void peek_stackSizeOne_returnsTopOfStack() throws Exception {
        setField(jsonWriter, "stackSize", 1);
        int[] stack = new int[32];
        stack[0] = 42;
        setField(jsonWriter, "stack", stack);

        int result = (int) peekMethod.invoke(jsonWriter);
        assertEquals(42, result);
    }

    @Test
    @Timeout(8000)
    void peek_stackSizeMultiple_returnsTopOfStack() throws Exception {
        setField(jsonWriter, "stackSize", 3);
        int[] stack = new int[32];
        stack[0] = 10;
        stack[1] = 20;
        stack[2] = 30;
        setField(jsonWriter, "stack", stack);

        int result = (int) peekMethod.invoke(jsonWriter);
        assertEquals(30, result);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonWriter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}