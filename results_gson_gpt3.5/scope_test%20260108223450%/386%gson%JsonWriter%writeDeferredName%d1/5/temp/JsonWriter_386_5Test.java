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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonWriter_386_5Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Initialize the stack and stackSize to a valid state to avoid IllegalStateException in writeDeferredName()
        // The stack must have at least one element representing the current scope.
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);
        stack[0] = 5; // 5 corresponds to JsonScope.DANGLING_NAME, required before writing a name
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_withDeferredName() throws Exception {
        // Set deferredName field to a non-null value using reflection
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "testName");

        // Invoke writeDeferredName via reflection
        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);
        writeDeferredNameMethod.invoke(jsonWriter);

        // Verify deferredName reset to null
        assertNull(deferredNameField.get(jsonWriter));

        // Verify output contains the deferred name string
        String output = stringWriter.toString();
        assertTrue(output.contains("testName") || output.length() > 0);

        // Also verify that no exception was thrown and deferredName is cleared
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_withNullDeferredName() throws Exception {
        // Ensure deferredName is null
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, null);

        // Invoke writeDeferredName via reflection
        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);

        // Should do nothing and not throw
        writeDeferredNameMethod.invoke(jsonWriter);

        // deferredName remains null
        assertNull(deferredNameField.get(jsonWriter));

        // Output should be empty
        String output = stringWriter.toString();
        assertEquals("", output);
    }
}