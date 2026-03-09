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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_379_6Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_EmptyObject() throws IOException {
        // Begin an object to set state to EMPTY_OBJECT
        jsonWriter.beginObject();
        // endObject should close the object and write '}'
        JsonWriter returned = jsonWriter.endObject();
        assertSame(jsonWriter, returned);
        // The output should be "{}"
        assertEquals("{}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndObject_NonEmptyObject() throws IOException {
        // Begin an object
        jsonWriter.beginObject();
        // Add a name and value to make it non-empty
        jsonWriter.name("key").value("value");
        // endObject should close the object and write '}'
        JsonWriter returned = jsonWriter.endObject();
        assertSame(jsonWriter, returned);
        // The output should be {"key":"value"}
        assertEquals("{\"key\":\"value\"}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndObject_ThrowsIOExceptionIfStackEmpty() throws Exception {
        // Use reflection to invoke close with invalid state to simulate error
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Clear stack to simulate invalid state
        // Use reflection to set stackSize to 0 forcibly
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 0);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
        }, "Expected InvocationTargetException due to stack underflow");

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        // Fix: The cause is IllegalStateException, not IOException
        assertTrue(cause instanceof IllegalStateException, "Cause should be IllegalStateException");
    }

    @Test
    @Timeout(8000)
    public void testEndObject_ReflectionInvoke() throws Exception {
        // Begin an object to set state to EMPTY_OBJECT
        jsonWriter.beginObject();

        Method endObjectMethod = JsonWriter.class.getDeclaredMethod("endObject");
        endObjectMethod.setAccessible(true);

        Object returned = endObjectMethod.invoke(jsonWriter);
        assertSame(jsonWriter, returned);
        assertEquals("{}", stringWriter.toString());
    }
}