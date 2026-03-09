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

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

class JsonWriterBeforeNameTest {

    private JsonWriter jsonWriter;
    private Writer mockWriter;
    private Method beforeNameMethod;

    @BeforeEach
    void setUp() throws Exception {
        mockWriter = mock(Writer.class);
        jsonWriter = new JsonWriter(mockWriter);

        beforeNameMethod = JsonWriter.class.getDeclaredMethod("beforeName");
        beforeNameMethod.setAccessible(true);

        // Initialize stack and stackSize properly before each test
        // Set stackSize to 1 and stack[0] to EMPTY_OBJECT (3) to avoid IllegalStateException on constructor
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);
        stack[0] = 3; // EMPTY_OBJECT
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);
    }

    private void setStackAndStackSize(int[] stack, int stackSize) throws Exception {
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] internalStack = (int[]) stackField.get(jsonWriter);
        System.arraycopy(stack, 0, internalStack, 0, stackSize);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, stackSize);
    }

    @Test
    @Timeout(8000)
    void beforeName_firstInObject_writesCommaAndCallsNewlineAndReplaceTop() throws Throwable {
        // NONEMPTY_OBJECT = 1
        setStackAndStackSize(new int[]{1}, 1);

        beforeNameMethod.invoke(jsonWriter);

        // verify comma written
        verify(mockWriter).write(',');

        // verify stack top replaced with DANGLING_NAME (6)
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stackAfter = (int[]) stackField.get(jsonWriter);
        assertEquals(6, stackAfter[0]);
    }

    @Test
    @Timeout(8000)
    void beforeName_emptyObject_noCommaWrittenCallsNewlineAndReplaceTop() throws Throwable {
        // EMPTY_OBJECT = 3
        setStackAndStackSize(new int[]{3}, 1);

        beforeNameMethod.invoke(jsonWriter);

        // verify no comma written
        verify(mockWriter, never()).write(',');

        // verify stack top replaced with DANGLING_NAME (6)
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stackAfter = (int[]) stackField.get(jsonWriter);
        assertEquals(6, stackAfter[0]);
    }

    @Test
    @Timeout(8000)
    void beforeName_notObject_throwsIllegalStateException() throws Throwable {
        // Use context != NONEMPTY_OBJECT and != EMPTY_OBJECT, e.g. 0 (EMPTY_DOCUMENT)
        setStackAndStackSize(new int[]{0}, 1);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            beforeNameMethod.invoke(jsonWriter);
        });
        assertTrue(thrown.getCause() instanceof IllegalStateException);
        assertEquals("Nesting problem.", thrown.getCause().getMessage());

        // verify no comma written
        verify(mockWriter, never()).write(',');
    }
}