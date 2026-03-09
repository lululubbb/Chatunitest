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

        // Initialize stackSize and stack to a valid state (EMPTY_DOCUMENT) before each test
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stack[0] = 0; // EMPTY_DOCUMENT
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);
    }

    @Test
    @Timeout(8000)
    void beginArray_basicInvocation_shouldOpenArray() throws IOException {
        JsonWriter returned = jsonWriter.beginArray();
        assertSame(jsonWriter, returned);
        assertEquals("[", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void beginArray_withDeferredName_shouldWriteNameAndOpenArray() throws Exception {
        // Use reflection to set deferredName field to simulate deferred name presence
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "testName");

        // Use reflection to set stack and stackSize to simulate state with an object expecting a name
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stack[0] = 5; // DANGLING_NAME = 5 (from JsonScope)
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        // Use reflection to set out field to a spy to verify writeDeferredName effects
        Field outField = JsonWriter.class.getDeclaredField("out");
        outField.setAccessible(true);
        StringWriter realWriter = new StringWriter();
        StringWriter spyWriter = spy(realWriter);
        outField.set(jsonWriter, spyWriter);

        // Invoke beginArray
        JsonWriter returned = jsonWriter.beginArray();

        // Verify returned instance
        assertSame(jsonWriter, returned);

        // Verify deferredName cleared
        assertNull(deferredNameField.get(jsonWriter));

        // Verify the output starts with the name and then the array open bracket
        String output = spyWriter.toString();
        // The name is written as a JSON string with quotes and colon, e.g. "\"testName\":"
        assertTrue(output.contains("\"testName\""));
        assertTrue(output.contains("["));
    }

    @Test
    @Timeout(8000)
    void beginArray_invokesPrivateWriteDeferredName() throws Exception {
        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);
        // Initially no deferredName, so should do nothing and not throw
        writeDeferredNameMethod.invoke(jsonWriter);

        // Set deferredName and stack to DANGLING_NAME to test writeDeferredName writes name
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "nameForDeferred");

        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stack[0] = 5; // DANGLING_NAME
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        Field outField = JsonWriter.class.getDeclaredField("out");
        outField.setAccessible(true);
        StringWriter realWriter = new StringWriter();
        StringWriter spyWriter = spy(realWriter);
        outField.set(jsonWriter, spyWriter);

        writeDeferredNameMethod.invoke(jsonWriter);

        String output = spyWriter.toString();
        assertTrue(output.contains("\"nameForDeferred\""));
        assertNull(deferredNameField.get(jsonWriter));
    }

    @Test
    @Timeout(8000)
    void beginArray_openMethodCalled_correctly() throws Exception {
        // Use reflection to invoke private open method directly to verify behavior
        Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
        openMethod.setAccessible(true);

        // Set stack and stackSize properly before calling open
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stack[0] = 0; // EMPTY_DOCUMENT
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        JsonWriter returned = (JsonWriter) openMethod.invoke(jsonWriter, 1 /* EMPTY_ARRAY */, '[');
        assertSame(jsonWriter, returned);

        // The output should contain '['
        String output = stringWriter.toString();
        assertEquals("[", output);
    }
}