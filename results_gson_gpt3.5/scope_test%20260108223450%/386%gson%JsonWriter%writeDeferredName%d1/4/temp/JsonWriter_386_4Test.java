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

public class JsonWriter_386_4Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Initialize stack and stackSize to valid state for writing a name
        setField(jsonWriter, "stackSize", 1);
        int[] stack = new int[32];
        stack[0] = 3; // 3 corresponds to JsonScope.EMPTY_OBJECT (internal state for object)
        setField(jsonWriter, "stack", stack);
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_nullDeferredName_doesNothing() throws Exception {
        setField(jsonWriter, "deferredName", null);

        // Invoke private method writeDeferredName via reflection
        Method method = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        method.setAccessible(true);
        method.invoke(jsonWriter);

        // deferredName remains null
        assertNull(getField(jsonWriter, "deferredName"));
        // No output is written
        assertEquals("", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_nonNullDeferredName_callsBeforeNameAndStringAndClearsDeferredName() throws Exception {
        String deferredNameValue = "testName";
        setField(jsonWriter, "deferredName", deferredNameValue);

        // Initialize stack and stackSize to valid state for writing a name
        setField(jsonWriter, "stackSize", 1);
        int[] stack = new int[32];
        stack[0] = 3; // 3 corresponds to JsonScope.EMPTY_OBJECT
        setField(jsonWriter, "stack", stack);

        // Spy on jsonWriter to verify private method calls via reflection
        JsonWriter spyWriter = Mockito.spy(jsonWriter);

        // Set deferredName on spy as well
        setField(spyWriter, "deferredName", deferredNameValue);

        // Invoke private method writeDeferredName via reflection
        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);
        writeDeferredNameMethod.invoke(spyWriter);

        // deferredName should be cleared
        assertNull(getField(spyWriter, "deferredName"));

        // The output should contain the deferredName as a JSON string
        String output = stringWriter.toString();
        assertTrue(output.contains("\"" + deferredNameValue + "\""));
    }

    // Helper method to set private fields via reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonWriter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // Helper method to get private fields via reflection
    private Object getField(Object target, String fieldName) throws Exception {
        Field field = JsonWriter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}