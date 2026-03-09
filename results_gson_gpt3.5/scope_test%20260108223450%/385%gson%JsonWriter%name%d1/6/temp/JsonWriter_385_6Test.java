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
import java.lang.reflect.Field;

public class JsonWriter_385_6Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
        // set stackSize to 1 (non-zero) to avoid "JsonWriter is closed." exception
        try {
            Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
            stackSizeField.setAccessible(true);
            stackSizeField.setInt(jsonWriter, 1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection setup failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void name_NullName_ThrowsNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> jsonWriter.name(null));
        assertEquals("name == null", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void name_DeferredNameNotNull_ThrowsIllegalStateException() throws Exception {
        // set deferredName to non-null to simulate state
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "existingName");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> jsonWriter.name("newName"));
        deferredNameField.set(jsonWriter, null);
        assertNotNull(exception);
    }

    @Test
    @Timeout(8000)
    void name_StackSizeZero_ThrowsIllegalStateException() throws Exception {
        // set stackSize to 0 to simulate closed writer
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 0);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> jsonWriter.name("name"));
        assertEquals("JsonWriter is closed.", exception.getMessage());

        // reset stackSize to 1 for other tests
        stackSizeField.setInt(jsonWriter, 1);
    }

    @Test
    @Timeout(8000)
    void name_ValidName_SetsDeferredNameAndReturnsThis() throws Exception {
        String testName = "myName";
        JsonWriter returned = jsonWriter.name(testName);

        assertSame(jsonWriter, returned);

        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        String deferredNameValue = (String) deferredNameField.get(jsonWriter);
        assertEquals(testName, deferredNameValue);
    }
}