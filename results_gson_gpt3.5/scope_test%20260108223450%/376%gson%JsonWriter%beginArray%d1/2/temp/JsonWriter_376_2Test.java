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
import java.lang.reflect.Method;

class JsonWriterBeginArrayTest {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Initialize stack and stackSize properly to avoid IllegalStateException in JsonWriter
        setField(jsonWriter, "stack", new int[32]);
        setField(jsonWriter, "stackSize", 0);

        // Push EMPTY_DOCUMENT to start state
        Method push = JsonWriter.class.getDeclaredMethod("push", int.class);
        push.setAccessible(true);
        push.invoke(jsonWriter, JsonScope.EMPTY_DOCUMENT);
    }

    @Test
    @Timeout(8000)
    void beginArray_shouldWriteOpenArrayAndReturnThis() throws IOException {
        JsonWriter returned = jsonWriter.beginArray();
        assertSame(jsonWriter, returned);
        assertEquals("[", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void beginArray_withDeferredName_shouldWriteNameBeforeArray() throws Exception {
        // Use reflection to set deferredName to a non-null value
        setField(jsonWriter, "deferredName", "testName");

        // Set stack and stackSize to simulate being inside an object
        int[] stack = new int[32];
        stack[0] = JsonScope.EMPTY_OBJECT;
        setField(jsonWriter, "stack", stack);
        setField(jsonWriter, "stackSize", 1);

        JsonWriter returned = jsonWriter.beginArray();

        assertSame(jsonWriter, returned);
        String output = stringWriter.toString();
        assertTrue(output.contains("\"testName\""));
        assertTrue(output.contains("["));
    }

    @Test
    @Timeout(8000)
    void writeDeferredName_invokedViaReflection_handlesNullDeferredName() throws Exception {
        // deferredName is null by default
        Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredName.setAccessible(true);
        // Should not throw
        writeDeferredName.invoke(jsonWriter);
    }

    @Test
    @Timeout(8000)
    void writeDeferredName_invokedViaReflection_handlesNonNullDeferredName() throws Exception {
        setField(jsonWriter, "deferredName", "name");

        int[] stack = new int[32];
        stack[0] = JsonScope.EMPTY_OBJECT;
        setField(jsonWriter, "stack", stack);
        setField(jsonWriter, "stackSize", 1);

        Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredName.setAccessible(true);
        writeDeferredName.invoke(jsonWriter);

        String output = stringWriter.toString();
        assertTrue(output.contains("\"name\""));
    }

    // Helper method to set private fields via reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}