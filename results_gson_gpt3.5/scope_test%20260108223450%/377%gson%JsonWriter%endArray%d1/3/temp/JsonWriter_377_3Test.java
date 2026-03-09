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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonWriter_377_3Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testEndArray_afterBeginArray_shouldWriteClosingBracket() throws IOException {
        jsonWriter.beginArray();
        jsonWriter.endArray();
        jsonWriter.flush();
        assertEquals("[]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndArray_afterBeginArrayWithValue_shouldWriteValuesAndClosingBracket() throws IOException {
        jsonWriter.beginArray();
        jsonWriter.value("value");
        jsonWriter.endArray();
        jsonWriter.flush();
        assertEquals("[\"value\"]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndArray_withoutBeginArray_shouldThrowIllegalStateException() throws IOException {
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            jsonWriter.endArray();
        });
        assertTrue(thrown.getMessage().contains("Nesting problem"));
    }

    @Test
    @Timeout(8000)
    public void testCloseMethodViaReflection_validStates_shouldReturnJsonWriter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Setup stack and stackSize to simulate EMPTY_ARRAY state
        setStackAndStackSize(jsonWriter, new int[]{JsonScope.EMPTY_ARRAY}, 1);
        Object result = closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
        assertSame(jsonWriter, result);

        // Setup stack and stackSize to simulate NONEMPTY_ARRAY state
        setStackAndStackSize(jsonWriter, new int[]{JsonScope.NONEMPTY_ARRAY}, 1);
        result = closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
        assertSame(jsonWriter, result);
    }

    @Test
    @Timeout(8000)
    public void testCloseMethodViaReflection_invalidState_shouldThrowIllegalStateException() throws NoSuchMethodException {
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        setStackAndStackSize(jsonWriter, new int[]{JsonScope.EMPTY_DOCUMENT}, 1);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
        });
        assertTrue(thrown.getCause() instanceof IllegalStateException);
        assertTrue(thrown.getCause().getMessage().contains("Nesting problem"));
    }

    private void setStackAndStackSize(JsonWriter writer, int[] stackValues, int stackSize) {
        try {
            var stackField = JsonWriter.class.getDeclaredField("stack");
            stackField.setAccessible(true);
            stackField.set(writer, stackValues);

            var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
            stackSizeField.setAccessible(true);
            stackSizeField.setInt(writer, stackSize);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}