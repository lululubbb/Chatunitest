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
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.StringWriter;
import java.lang.reflect.Method;

class JsonWriter_375_4Test {

    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        jsonWriter = new JsonWriter(new StringWriter());
    }

    @Test
    @Timeout(8000)
    void testGetSerializeNulls_defaultTrue() {
        assertTrue(jsonWriter.getSerializeNulls());
    }

    @Test
    @Timeout(8000)
    void testGetSerializeNulls_afterSetSerializeNullsTrue() {
        jsonWriter.setSerializeNulls(true);
        assertTrue(jsonWriter.getSerializeNulls());
    }

    @Test
    @Timeout(8000)
    void testGetSerializeNulls_afterSetSerializeNullsFalse() {
        jsonWriter.setSerializeNulls(false);
        assertFalse(jsonWriter.getSerializeNulls());
    }

    @Test
    @Timeout(8000)
    void testGetSerializeNulls_reflectiveAccess() throws Exception {
        // Access private field serializeNulls via reflection to toggle value
        var field = JsonWriter.class.getDeclaredField("serializeNulls");
        field.setAccessible(true);

        field.setBoolean(jsonWriter, false);
        Method method = JsonWriter.class.getDeclaredMethod("getSerializeNulls");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(jsonWriter);
        assertFalse(result);

        field.setBoolean(jsonWriter, true);
        result = (boolean) method.invoke(jsonWriter);
        assertTrue(result);
    }
}