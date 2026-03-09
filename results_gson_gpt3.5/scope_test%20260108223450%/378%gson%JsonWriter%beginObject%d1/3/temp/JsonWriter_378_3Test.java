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

public class JsonWriter_378_3Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_NormalFlow() throws IOException {
        JsonWriter writer = jsonWriter.beginObject();
        assertNotNull(writer);
        assertSame(jsonWriter, writer);
        // The output should begin with '{'
        String output = stringWriter.toString();
        assertTrue(output.startsWith("{"));
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_DeferredNameWritten() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Use reflection to set deferredName to a non-null value to test writeDeferredName() behavior
        var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "myName");

        // Also set stack and stackSize to simulate state so writeDeferredName does not throw
        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);
        stack[0] = JsonScope.EMPTY_OBJECT;
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        // Call beginObject, which will call writeDeferredName and then open
        JsonWriter writer = jsonWriter.beginObject();
        assertNotNull(writer);
        assertSame(jsonWriter, writer);

        // The output should contain the name and then '{'
        String output = stringWriter.toString();
        assertTrue(output.contains("myName"));
        assertTrue(output.contains("{"));
    }

    @Test
    @Timeout(8000)
    public void testOpen_AddsToStackAndWritesChar() throws Exception {
        // Reset stackSize to 0 before test to avoid interference from previous tests
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 0);

        // Reset 'closed' field to false to avoid IllegalStateException: JsonWriter is closed.
        // 'closed' is private boolean, so set it to false via reflection
        try {
            var closedField = JsonWriter.class.getDeclaredField("closed");
            closedField.setAccessible(true);
            closedField.setBoolean(jsonWriter, false);
        } catch (NoSuchFieldException ignored) {
            // If field does not exist, ignore
        }

        // Also reset 'stack' array to avoid invalid state
        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);
        for (int i = 0; i < stack.length; i++) {
            stack[i] = 0;
        }

        // Use reflection to invoke private open method with EMPTY_OBJECT and '{'
        Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
        openMethod.setAccessible(true);

        JsonWriter result = (JsonWriter) openMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, '{');
        assertSame(jsonWriter, result);

        // Check stackSize increased to 1
        int stackSize = stackSizeField.getInt(jsonWriter);
        assertEquals(1, stackSize);

        // Check stack top is EMPTY_OBJECT
        assertEquals(JsonScope.EMPTY_OBJECT, stack[stackSize - 1]);

        // Check output contains '{'
        String output = stringWriter.toString();
        assertTrue(output.contains("{"));
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_NullDeferredNameDoesNothing() throws Exception {
        // deferredName is null by default; invoking writeDeferredName should do nothing
        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);

        // Call without deferredName set
        writeDeferredNameMethod.invoke(jsonWriter);

        // Output should be empty
        assertEquals("", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_WithDeferredName() throws Exception {
        // Set deferredName field to a string
        var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "name");

        // Set stack and stackSize to simulate state so writeDeferredName writes name
        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);
        stack[0] = JsonScope.EMPTY_OBJECT;
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);

        writeDeferredNameMethod.invoke(jsonWriter);

        // deferredName should be set to null after writeDeferredName
        assertNull(deferredNameField.get(jsonWriter));

        // Output should contain the name string (escaped with quotes)
        String output = stringWriter.toString();
        assertTrue(output.contains("\"name\""));
    }
}