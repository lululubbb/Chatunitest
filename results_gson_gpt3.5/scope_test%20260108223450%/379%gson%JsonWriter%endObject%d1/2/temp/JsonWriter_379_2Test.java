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

public class JsonWriter_379_2Test {
    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_whenEmptyObject() throws IOException {
        jsonWriter.beginObject();
        JsonWriter returned = jsonWriter.endObject();
        assertSame(jsonWriter, returned);
        // The output should be "{}"
        assertEquals("{}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndObject_whenNonEmptyObject() throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("key").value("value");
        JsonWriter returned = jsonWriter.endObject();
        assertSame(jsonWriter, returned);
        // The output should be {"key":"value"}
        assertEquals("{\"key\":\"value\"}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndObject_illegalState_shouldThrow() throws IOException {
        // Calling endObject without beginObject should throw IllegalStateException
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            jsonWriter.endObject();
        });
        String msg = exception.getMessage();
        assertNotNull(msg);
    }

    @Test
    @Timeout(8000)
    public void testCloseMethodDirectlyUsingReflection() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // We test private close(int empty, int nonempty, char closeBracket) method via reflection
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Setup: push EMPTY_OBJECT to stack so close can work
        Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        // Push EMPTY_OBJECT to stack to simulate beginObject
        pushMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT);

        Object returned = closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
        assertSame(jsonWriter, returned);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_withIndentAndLenient() throws IOException {
        jsonWriter.setIndent("  ");
        jsonWriter.setLenient(true);
        jsonWriter.beginObject();
        jsonWriter.name("a").value("b");
        JsonWriter returned = jsonWriter.endObject();
        assertSame(jsonWriter, returned);
        String output = stringWriter.toString();
        assertTrue(output.contains("a"));
        assertTrue(output.contains("b"));
        assertTrue(output.contains("}"));
    }
}