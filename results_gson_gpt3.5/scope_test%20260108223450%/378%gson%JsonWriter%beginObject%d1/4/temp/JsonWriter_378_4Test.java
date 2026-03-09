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
import com.google.gson.stream.JsonScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class JsonWriter_378_4Test {
    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Initialize stackSize to 1 and stack[0] to EMPTY_DOCUMENT to avoid IllegalStateException on beginObject
        try {
            Field stackField = JsonWriter.class.getDeclaredField("stack");
            stackField.setAccessible(true);
            int[] stack = new int[32];
            stack[0] = JsonScope.EMPTY_DOCUMENT;
            stackField.set(jsonWriter, stack);

            Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
            stackSizeField.setAccessible(true);
            stackSizeField.setInt(jsonWriter, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_Normal() throws IOException {
        JsonWriter writer = jsonWriter.beginObject();
        assertSame(jsonWriter, writer);
        assertEquals("{", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_WithDeferredName() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Use reflection to set deferredName to a non-null value
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "name");

        // Also set stack and stackSize to valid state to avoid errors inside open and beforeName
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stack[0] = JsonScope.NONEMPTY_OBJECT; // set to NONEMPTY_OBJECT to allow a name to be written
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        // Also set the separator field to ":" to avoid NPE inside beforeName
        Field separatorField = JsonWriter.class.getDeclaredField("separator");
        separatorField.setAccessible(true);
        separatorField.set(jsonWriter, ":");

        // We need to call beginObject and check it writes the object start correctly
        JsonWriter writer = jsonWriter.beginObject();
        assertSame(jsonWriter, writer);
        String output = stringWriter.toString();
        assertTrue(output.startsWith("{"));
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_PrivateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, NoSuchFieldException {
        // Use reflection to invoke private writeDeferredName method
        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);

        // deferredName null case: should do nothing and not throw
        writeDeferredNameMethod.invoke(jsonWriter);

        // deferredName set case: set deferredName and stack state to avoid exceptions
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "testName");

        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stack[0] = JsonScope.NONEMPTY_OBJECT;
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        // Also set the separator field to ":" to avoid NPE inside beforeName
        Field separatorField = JsonWriter.class.getDeclaredField("separator");
        separatorField.setAccessible(true);
        separatorField.set(jsonWriter, ":");

        writeDeferredNameMethod.invoke(jsonWriter);

        String output = stringWriter.toString();
        assertTrue(output.contains("testName") || output.contains(":"));
    }

    @Test
    @Timeout(8000)
    public void testOpen_PrivateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
        openMethod.setAccessible(true);

        // Call open with EMPTY_OBJECT and '{' and verify it returns JsonWriter instance
        Object result = openMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, '{');
        assertSame(jsonWriter, result);
        String output = stringWriter.toString();
        assertTrue(output.startsWith("{"));
    }
}