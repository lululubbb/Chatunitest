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

public class JsonWriter_379_3Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_afterBeginObject_shouldCloseObject() throws IOException {
        jsonWriter.beginObject();
        JsonWriter returned = jsonWriter.endObject();
        assertSame(jsonWriter, returned);
        assertEquals("{}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndObject_afterBeginObject_withName_shouldCloseObjectProperly() throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("key");
        jsonWriter.value("value");
        JsonWriter returned = jsonWriter.endObject();
        assertSame(jsonWriter, returned);
        assertEquals("{\"key\":\"value\"}", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testEndObject_illegalState_shouldThrow() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            jsonWriter.endObject();
        });
        assertTrue(exception.getMessage().contains("Nesting problem"));
    }

    @Test
    @Timeout(8000)
    public void testCloseMethod_invokedByEndObject_withReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // Use reflection to invoke private close method with parameters matching endObject call
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        jsonWriter.beginObject();

        Object returned = closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
        assertSame(jsonWriter, returned);
        assertEquals("{}", stringWriter.toString());
    }
}