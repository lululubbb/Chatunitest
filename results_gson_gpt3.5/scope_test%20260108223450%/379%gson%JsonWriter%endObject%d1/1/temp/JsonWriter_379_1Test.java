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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonWriter_379_1Test {

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
        assertEquals("{}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndObject_whenNonEmptyObject() throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("key").value("value");
        JsonWriter returned = jsonWriter.endObject();
        assertSame(jsonWriter, returned);
        assertEquals("{\"key\":\"value\"}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndObject_reflectiveInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // Use reflection to invoke private close method with parameters matching endObject's call
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Setup stack to simulate EMPTY_OBJECT state
        // Because endObject calls close(EMPTY_OBJECT, NONEMPTY_OBJECT, '}')
        // We'll test both EMPTY_OBJECT and NONEMPTY_OBJECT cases via reflection

        // EMPTY_OBJECT case
        JsonWriter writer1 = new JsonWriter(new StringWriter());
        // push EMPTY_OBJECT on stack to simulate state
        Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(writer1, JsonScope.EMPTY_OBJECT);

        Object ret1 = closeMethod.invoke(writer1, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
        assertSame(writer1, ret1);

        // NONEMPTY_OBJECT case
        JsonWriter writer2 = new JsonWriter(new StringWriter());
        pushMethod.invoke(writer2, JsonScope.NONEMPTY_OBJECT);

        Object ret2 = closeMethod.invoke(writer2, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
        assertSame(writer2, ret2);
    }
}