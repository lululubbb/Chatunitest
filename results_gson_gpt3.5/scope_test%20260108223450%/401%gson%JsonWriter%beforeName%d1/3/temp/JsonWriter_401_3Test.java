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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.io.IOException;

class JsonWriterBeforeNameTest {

    private JsonWriter jsonWriter;
    private Writer mockWriter;
    private Method beforeNameMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        mockWriter = mock(Writer.class);
        jsonWriter = new JsonWriter(mockWriter);

        beforeNameMethod = JsonWriter.class.getDeclaredMethod("beforeName");
        beforeNameMethod.setAccessible(true);
    }

    private void setStackAndSize(int[] stack, int stackSize) throws Exception {
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, stackSize);
    }

    @Test
    @Timeout(8000)
    void beforeName_firstInObject_writesCommaAndCallsNewlineAndReplaceTop() throws Throwable {
        // Setup stack so peek() returns NONEMPTY_OBJECT (2)
        setStackAndSize(new int[]{2}, 1);

        beforeNameMethod.invoke(jsonWriter);

        // Verify that out.write(',') was called
        verify(mockWriter).write(',');

        // Verify stack top replaced with DANGLING_NAME (3)
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);
        assertEquals(3, stack[0]);
    }

    @Test
    @Timeout(8000)
    void beforeName_emptyObject_callsNewlineAndReplaceTop() throws Throwable {
        // Setup stack so peek() returns EMPTY_OBJECT (1)
        setStackAndSize(new int[]{1}, 1);

        beforeNameMethod.invoke(jsonWriter);

        // Should NOT write ',' because context == EMPTY_OBJECT
        verify(mockWriter, never()).write(',');

        // Verify stack top replaced with DANGLING_NAME (3)
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);
        assertEquals(3, stack[0]);
    }

    @Test
    @Timeout(8000)
    void beforeName_notInObject_throwsIllegalStateException() throws Throwable {
        // Setup stack so peek() returns something else (e.g. 0)
        setStackAndSize(new int[]{0}, 1);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            beforeNameMethod.invoke(jsonWriter);
        });
        assertTrue(thrown.getCause() instanceof IllegalStateException);
        assertEquals("Nesting problem.", thrown.getCause().getMessage());

        // Verify no write calls to out
        verify(mockWriter, never()).write(anyInt());
    }
}