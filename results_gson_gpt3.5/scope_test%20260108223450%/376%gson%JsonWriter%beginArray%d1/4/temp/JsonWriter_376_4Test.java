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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonWriter_376_4Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void beginArray_shouldWriteOpenBracketAndReturnJsonWriter() throws IOException {
        JsonWriter writer = jsonWriter.beginArray();
        assertSame(jsonWriter, writer);
        assertEquals("[", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void beginArray_shouldCallWriteDeferredName() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to invoke private writeDeferredName and verify it does not throw
        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);
        // writeDeferredName is called inside beginArray, so just call beginArray and check no exception
        jsonWriter.beginArray();
        // Also invoke writeDeferredName directly to cover it
        writeDeferredNameMethod.invoke(jsonWriter);
    }

    @Test
    @Timeout(8000)
    public void beginArray_shouldOpenEmptyArrayScope() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to invoke private open method with EMPTY_ARRAY and '['
        Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
        openMethod.setAccessible(true);
        JsonWriter writer = (JsonWriter) openMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, '[');
        assertSame(jsonWriter, writer);
        // The output should contain "[" after open called
        assertEquals("[", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void beginArray_shouldPushEmptyArrayScope() throws IOException, NoSuchFieldException, IllegalAccessException {
        // invoke beginArray and verify stack contains EMPTY_ARRAY at top
        jsonWriter.beginArray();

        // Use reflection to access private field stack and stackSize
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = (int) stackSizeField.get(jsonWriter);

        assertTrue(stackSize > 0);
        assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);
    }

    @Test
    @Timeout(8000)
    public void beginArray_withDeferredName_shouldWriteNameBeforeArray() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Use reflection to set deferredName to a value
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "myName");

        // Use reflection to set indent to test indentation and newline
        Field indentField = JsonWriter.class.getDeclaredField("indent");
        indentField.setAccessible(true);
        indentField.set(jsonWriter, "  ");

        // Use reflection to set stack and stackSize to simulate inside an object (to avoid nesting problem)
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        // Set stackSize to 1 and stack[0] to EMPTY_OBJECT to simulate being inside an object
        stackSizeField.setInt(jsonWriter, 1);
        stack[0] = JsonScope.EMPTY_OBJECT;

        // Call beginArray, which should write deferredName before '['
        jsonWriter.beginArray();

        String output = stringWriter.toString();
        assertTrue(output.contains("myName"));
        assertTrue(output.contains("["));
    }
}