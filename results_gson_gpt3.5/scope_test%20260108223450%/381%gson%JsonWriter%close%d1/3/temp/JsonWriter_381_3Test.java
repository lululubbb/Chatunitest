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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_381_3Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testClose_withEmptyContext() throws Exception {
        // Set up stack and stackSize for EMPTY_OBJECT context
        setStack(new int[]{JsonScope.EMPTY_OBJECT}, 1);
        setDeferredName(null);

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        JsonWriter result = (JsonWriter) closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');

        assertSame(jsonWriter, result);
        assertEquals(0, getStackSize());
        assertEquals("}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testClose_withNonEmptyContext() throws Exception {
        // Set up stack and stackSize for NONEMPTY_OBJECT context
        setStack(new int[]{JsonScope.NONEMPTY_OBJECT}, 1);
        setDeferredName(null);

        // Spy on jsonWriter to verify newline() call via reflection
        JsonWriter spyWriter = spy(jsonWriter);

        // We need to replace the 'out' field in spyWriter to use the same stringWriter to capture output
        // Because spying may cause the original 'out' to be replaced or wrapped, set it explicitly
        var outField = JsonWriter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(spyWriter, stringWriter);

        // Also set stack and stackSize on spyWriter to match spy state
        setStack(spyWriter, new int[]{JsonScope.NONEMPTY_OBJECT}, 1);
        setDeferredName(spyWriter, null);

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Because close() throws IOException, wrap invocation in try-catch and rethrow as RuntimeException for test
        JsonWriter result = (JsonWriter) closeMethod.invoke(spyWriter, JsonScope.NONEMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');

        assertSame(spyWriter, result);
        assertEquals(0, getStackSize(spyWriter));

        // Verify output contains newline before '}'
        String output = stringWriter.toString();
        assertTrue(output.endsWith("}"), "Output should end with '}'");
        // The newline is added before the closing bracket, so output length should be at least 2
        assertTrue(output.length() >= 2, "Output should be at least 2 characters long");

        // Check that the character before '}' is a newline character
        char beforeLastChar = output.charAt(output.length() - 2);
        assertTrue(beforeLastChar == '\n' || beforeLastChar == '\r', "Output should contain a newline before '}'");
    }

    @Test
    @Timeout(8000)
    public void testClose_withInvalidContext_throwsIllegalStateException() throws Exception {
        setStack(new int[]{JsonScope.EMPTY_DOCUMENT}, 1);
        setDeferredName(null);

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () ->
            closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}')
        );
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertEquals("Nesting problem.", cause.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testClose_withDeferredName_throwsIllegalStateException() throws Exception {
        setStack(new int[]{JsonScope.EMPTY_OBJECT}, 1);
        setDeferredName("name");

        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () ->
            closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}')
        );
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertEquals("Dangling name: name", cause.getMessage());
    }

    // Helper methods to set private fields via reflection

    private void setStack(int[] stack, int stackSize) throws Exception {
        setStack(jsonWriter, stack, stackSize);
    }

    private void setStack(JsonWriter writer, int[] stack, int stackSize) throws Exception {
        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(writer, stack);

        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(writer, stackSize);
    }

    private int getStackSize() throws Exception {
        return getStackSize(jsonWriter);
    }

    private int getStackSize(JsonWriter writer) throws Exception {
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        return stackSizeField.getInt(writer);
    }

    private void setDeferredName(String name) throws Exception {
        setDeferredName(jsonWriter, name);
    }

    private void setDeferredName(JsonWriter writer, String name) throws Exception {
        var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(writer, name);
    }
}