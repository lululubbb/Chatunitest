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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonWriter_386_2Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_NullDeferredName() throws Exception {
        // deferredName is null by default, so just invoke writeDeferredName and expect no exception
        Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredName.setAccessible(true);

        // Should not throw and output should be empty
        writeDeferredName.invoke(jsonWriter);
        assertEquals("", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_WithDeferredName() throws Exception {
        // Begin an object to set proper internal state and avoid "Nesting problem"
        jsonWriter.beginObject();

        // Set deferredName field to a non-null value using reflection
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, "testName");

        Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredName.setAccessible(true);

        // Invoke writeDeferredName
        writeDeferredName.invoke(jsonWriter);

        // deferredName should be null after writing
        assertNull(deferredNameField.get(jsonWriter));

        // Output should contain the JSON string of the deferred name
        String output = stringWriter.toString();
        assertTrue(output.contains("\"testName\""), "Output should contain the deferred name as a JSON string");
    }
}