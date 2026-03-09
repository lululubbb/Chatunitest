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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonWriter_386_6Test {
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    private Method writeDeferredNameMethod;
    private Method beforeNameMethod;
    private Method stringMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, IOException, Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Begin an object to set correct internal state (stack) and avoid nesting problem
        jsonWriter.beginObject();

        // Access private methods via reflection
        writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);

        beforeNameMethod = JsonWriter.class.getDeclaredMethod("beforeName");
        beforeNameMethod.setAccessible(true);

        stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
        stringMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_whenDeferredNameIsNull() throws Throwable {
        // deferredName is null by default
        // invoke private method writeDeferredName
        writeDeferredNameMethod.invoke(jsonWriter);

        // Since deferredName is null, nothing should be written to the output except '{' from beginObject
        // So output should be just "{"
        assertEquals("{", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_whenDeferredNameIsSet() throws Throwable {
        // Set deferredName via reflection
        setDeferredName("testName");

        // Invoke writeDeferredName on jsonWriter via reflection
        writeDeferredNameMethod.invoke(jsonWriter);

        // After invocation, deferredName should be null
        assertNull(getDeferredName(jsonWriter));

        // The output should contain the string representation of deferredName (quoted)
        String output = stringWriter.toString();
        assertTrue(output.contains("\"testName\""));
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_callsBeforeNameAndString() throws Throwable {
        // Set deferredName to a known value
        setDeferredName("myName");

        // Invoke writeDeferredName on jsonWriter
        writeDeferredNameMethod.invoke(jsonWriter);

        // Verify deferredName is null
        assertNull(getDeferredName(jsonWriter));

        // Verify output contains the deferredName string (quoted)
        String output = stringWriter.toString();
        assertTrue(output.contains("\"myName\""));
    }

    // Helper method to set private deferredName field via reflection
    private void setDeferredName(String name) throws Exception {
        java.lang.reflect.Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, name);
    }

    // Helper method to get private deferredName field via reflection
    private String getDeferredName(JsonWriter writer) throws Exception {
        java.lang.reflect.Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        return (String) deferredNameField.get(writer);
    }
}