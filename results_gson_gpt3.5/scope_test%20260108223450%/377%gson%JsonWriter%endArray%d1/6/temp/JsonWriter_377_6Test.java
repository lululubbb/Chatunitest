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

public class JsonWriter_377_6Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void endArray_shouldCloseEmptyArray() throws IOException {
        // beginArray to push EMPTY_ARRAY state
        jsonWriter.beginArray();
        // endArray should close the array and append ']'
        JsonWriter returned = jsonWriter.endArray();
        assertSame(jsonWriter, returned);
        assertEquals("[]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void endArray_shouldCloseNonEmptyArray() throws IOException {
        jsonWriter.beginArray();
        jsonWriter.value("value");
        JsonWriter returned = jsonWriter.endArray();
        assertSame(jsonWriter, returned);
        assertEquals("[\"value\"]", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void endArray_shouldThrowIOExceptionIfStackEmpty() {
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            jsonWriter.endArray();
        });
        assertTrue(thrown.getMessage().contains("Nesting problem"));
    }

    @Test
    @Timeout(8000)
    void endArray_privateCloseMethodBehavior() throws Throwable {
        // Use reflection to invoke private close method with EMPTY_ARRAY
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Prepare jsonWriter with beginArray to push EMPTY_ARRAY state
        jsonWriter.beginArray();
        // close EMPTY_ARRAY, NONEMPTY_ARRAY, ']'
        JsonWriter returned = (JsonWriter) closeMethod.invoke(jsonWriter, 1 /*EMPTY_ARRAY*/, 2 /*NONEMPTY_ARRAY*/, ']');
        assertSame(jsonWriter, returned);
        assertEquals("[]", stringWriter.toString());

        // Create a new JsonWriter and beginArray + value to switch to NONEMPTY_ARRAY
        StringWriter sw2 = new StringWriter();
        JsonWriter jw2 = new JsonWriter(sw2);
        jw2.beginArray();
        jw2.value("test");
        // close NONEMPTY_ARRAY, NONEMPTY_ARRAY, ']'
        JsonWriter returned2 = (JsonWriter) closeMethod.invoke(jw2, 2 /*NONEMPTY_ARRAY*/, 2 /*NONEMPTY_ARRAY*/, ']');
        assertSame(jw2, returned2);
        assertEquals("[\"test\"]", sw2.toString());
    }
}