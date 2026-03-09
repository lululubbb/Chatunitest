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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_379_5Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void endObject_shouldCloseEmptyObject() throws Exception {
        // Arrange: begin an object so stack is set
        jsonWriter.beginObject();

        // Act: call endObject()
        JsonWriter returned = jsonWriter.endObject();

        // Assert: returned is the same instance
        assertSame(jsonWriter, returned);

        // The underlying writer should contain "{}"
        assertEquals("{}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void endObject_shouldCloseNonEmptyObject() throws Exception {
        // Arrange: begin an object, add a name and value pair
        jsonWriter.beginObject();
        jsonWriter.name("key");
        jsonWriter.value("value");

        // Act: call endObject()
        JsonWriter returned = jsonWriter.endObject();

        // Assert: returned is the same instance
        assertSame(jsonWriter, returned);

        // The underlying writer should contain {"key":"value"}
        assertEquals("{\"key\":\"value\"}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void endObject_shouldThrowIOExceptionIfStackEmpty() throws Exception {
        // Using reflection to invoke private close method with invalid stack state
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Clear stack to simulate invalid state
        // Reflection to set stackSize = 0 forcibly to simulate empty stack
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 0);

        // Also forcibly set deferredName to null to avoid other issues
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, null);

        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
            } catch (Exception e) {
                // Unwrap InvocationTargetException cause
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw cause;
                }
                throw e;
            }
        });
        assertNotNull(thrown);
    }

    @Test
    @Timeout(8000)
    void endObject_shouldThrowIOExceptionIfTopStackInvalid() throws Exception {
        // Arrange: forcibly set invalid top stack value
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stackArray = (int[]) stackField.get(jsonWriter);
        stackArray[0] = JsonScope.EMPTY_ARRAY; // invalid for endObject
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        // Also forcibly set deferredName to null to avoid other issues
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, null);

        // Using reflection to invoke private close method with invalid top stack value
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
            } catch (Exception e) {
                // Unwrap InvocationTargetException cause
                Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw cause;
                }
                throw e;
            }
        });
        assertNotNull(thrown);
    }
}