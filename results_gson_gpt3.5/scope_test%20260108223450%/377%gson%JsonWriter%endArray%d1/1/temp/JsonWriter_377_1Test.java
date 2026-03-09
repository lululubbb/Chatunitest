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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.stream.JsonWriter;

public class JsonWriter_377_1Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
        // Reset stackSize to 0 explicitly (should be by constructor, but just in case)
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 0);
    }

    @Test
    @Timeout(8000)
    public void testEndArray_EmptyArray() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Begin an array to push EMPTY_ARRAY on stack
        jsonWriter.beginArray();

        // Use reflection to call private peek method to check stack state before endArray
        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        int topBefore = (int) peekMethod.invoke(jsonWriter);
        assertEquals(1, topBefore);

        // Call endArray
        JsonWriter returned = jsonWriter.endArray();
        assertSame(jsonWriter, returned);

        // After endArray stack size should be 0
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = (int) stackSizeField.get(jsonWriter);
        assertEquals(0, stackSize);

        // The output should contain ']'
        assertEquals("]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndArray_NonEmptyArray() throws IOException, NoSuchFieldException, IllegalAccessException {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter); // recreate to reset closed flag
        jsonWriter.beginArray();
        jsonWriter.value("value");

        // endArray should close NONEMPTY_ARRAY with ']'
        JsonWriter returned = jsonWriter.endArray();
        assertSame(jsonWriter, returned);

        // The output should contain the value and then ']'
        String output = stringWriter.toString();
        assertTrue(output.contains("value"));
        assertTrue(output.endsWith("]"));

        // stackSize should be 0 after endArray
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = (int) stackSizeField.get(jsonWriter);
        assertEquals(0, stackSize);
    }

    @Test
    @Timeout(8000)
    public void testEndArray_ThrowsIOExceptionWhenStackIsEmpty() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // The stack is empty initially, calling endArray should cause IllegalStateException internally in close
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            jsonWriter.endArray();
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testPrivateCloseMethod_EmptyArray() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter); // recreate to reset closed flag
        // Use reflection to invoke private close method with EMPTY_ARRAY
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        jsonWriter.beginArray();

        // close(EMPTY_ARRAY, NONEMPTY_ARRAY, ']') should remove the array from stack and write ']'
        JsonWriter returned = (JsonWriter) closeMethod.invoke(jsonWriter, 1 /* EMPTY_ARRAY */, 2 /* NONEMPTY_ARRAY */, ']');
        assertSame(jsonWriter, returned);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = (int) stackSizeField.get(jsonWriter);
        assertEquals(0, stackSize);

        assertEquals("]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrivateCloseMethod_NonEmptyArray() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter); // recreate to reset closed flag
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        jsonWriter.beginArray();
        jsonWriter.value("v");

        JsonWriter returned = (JsonWriter) closeMethod.invoke(jsonWriter, 2 /* NONEMPTY_ARRAY */, 2 /* NONEMPTY_ARRAY */, ']');
        assertSame(jsonWriter, returned);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = (int) stackSizeField.get(jsonWriter);
        assertEquals(0, stackSize);

        String output = stringWriter.toString();
        assertTrue(output.contains("v"));
        assertTrue(output.endsWith("]"));
    }

}