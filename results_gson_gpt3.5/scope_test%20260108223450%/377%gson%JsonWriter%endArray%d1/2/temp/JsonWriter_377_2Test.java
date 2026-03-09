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

public class JsonWriter_377_2Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Initialize stack and stackSize properly to avoid "JsonWriter is closed." errors
        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        int[] stackArray = (int[]) stackField.get(jsonWriter);
        stackArray[0] = 0; // EMPTY_DOCUMENT
        stackSizeField.setInt(jsonWriter, 1);

        // Reset closed flag if exists
        try {
            var closedField = JsonWriter.class.getDeclaredField("closed");
            closedField.setAccessible(true);
            closedField.setBoolean(jsonWriter, false);
        } catch (NoSuchFieldException ignored) {
        }
    }

    @Test
    @Timeout(8000)
    public void testEndArray_afterBeginArray() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Remove any pre-set stack and stackSize from setUp to avoid conflict,
        // because beginArray() will push to stack and expects correct state.
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 0);

        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stackArray = (int[]) stackField.get(jsonWriter);
        // Clear stack to zeroes
        for (int i = 0; i < stackArray.length; i++) {
            stackArray[i] = 0;
        }

        jsonWriter.beginArray();
        JsonWriter returned = jsonWriter.endArray();
        assertSame(jsonWriter, returned);
        assertEquals("[]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndArray_closeMethodInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, NoSuchFieldException {
        // Use reflection to invoke private close method with parameters matching endArray call
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Setup stack to simulate inside an array
        // private fields stack and stackSize need to be set via reflection
        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        int[] stackArray = (int[]) stackField.get(jsonWriter);

        // Set stackSize to 1 and stack[0] to EMPTY_ARRAY (1) to cover close method branch
        stackSizeField.setInt(jsonWriter, 1);
        stackArray[0] = 1; // EMPTY_ARRAY

        // Reset closed flag if exists (to avoid "JsonWriter is closed." error)
        try {
            var closedField = JsonWriter.class.getDeclaredField("closed");
            closedField.setAccessible(true);
            closedField.setBoolean(jsonWriter, false);
        } catch (NoSuchFieldException ignored) {
        }

        Object resultEmpty = closeMethod.invoke(jsonWriter, 1, 2, ']');
        assertSame(jsonWriter, resultEmpty);

        stackArray[0] = 2; // NONEMPTY_ARRAY

        // Reset closed flag again before second invocation
        try {
            var closedField = JsonWriter.class.getDeclaredField("closed");
            closedField.setAccessible(true);
            closedField.setBoolean(jsonWriter, false);
        } catch (NoSuchFieldException ignored) {
        }

        Object resultNonEmpty = closeMethod.invoke(jsonWriter, 1, 2, ']');
        assertSame(jsonWriter, resultNonEmpty);
    }

    @Test
    @Timeout(8000)
    public void testEndArray_withoutBeginArray_throwsException() throws Exception {
        // The close method should throw IllegalStateException if stack is empty or top is not array
        // Use reflection to clear stackSize to 0 to simulate error
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 0);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonWriter.endArray());
        assertTrue(thrown.getMessage().contains("Nesting problem"));
    }
}