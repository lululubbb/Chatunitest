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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

public class JsonWriter_374_1Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testSetSerializeNulls_true() throws NoSuchFieldException, IllegalAccessException {
        jsonWriter.setSerializeNulls(true);

        Field serializeNullsField = JsonWriter.class.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        boolean actual = serializeNullsField.getBoolean(jsonWriter);

        assertTrue(actual);
    }

    @Test
    @Timeout(8000)
    public void testSetSerializeNulls_false() throws NoSuchFieldException, IllegalAccessException {
        jsonWriter.setSerializeNulls(false);

        Field serializeNullsField = JsonWriter.class.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        boolean actual = serializeNullsField.getBoolean(jsonWriter);

        assertFalse(actual);
    }
}