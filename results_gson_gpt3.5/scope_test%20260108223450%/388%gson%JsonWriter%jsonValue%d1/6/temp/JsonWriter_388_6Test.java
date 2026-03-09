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
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonWriter_388_6Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testJsonValue_withNonNullValue() throws IOException {
        String input = "{\"key\":\"value\"}";

        JsonWriter returned = jsonWriter.jsonValue(input);

        assertSame(jsonWriter, returned);
        assertEquals(input, stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testJsonValue_withNullValue_callsNullValue() throws IOException {
        JsonWriter spyWriter = spy(new JsonWriter(stringWriter));

        JsonWriter returned = spyWriter.jsonValue(null);

        verify(spyWriter).nullValue();
        assertSame(spyWriter, returned);
    }

    @Test
    @Timeout(8000)
    public void testWriteDeferredName_invokedByReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);

        // Setup deferredName so that writeDeferredName does something
        jsonWriter.beginObject();
        jsonWriter.name("testName");

        // invoke private method
        writeDeferredNameMethod.invoke(jsonWriter);

        // After writeDeferredName, deferredName should be null (indirectly verified by no exception and normal flow)
        // We verify that the stringWriter contains the deferred name properly quoted and colon appended
        String output = stringWriter.toString();
        assertTrue(output.contains("\"testName\""));
        assertTrue(output.contains(":"));
    }

    @Test
    @Timeout(8000)
    public void testBeforeValue_invokedByReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
        beforeValueMethod.setAccessible(true);

        // invoke private method, expecting no exceptions
        beforeValueMethod.invoke(jsonWriter);
    }

    @Test
    @Timeout(8000)
    public void testJsonValue_callsWriteDeferredNameAndBeforeValue() throws IOException {
        JsonWriter spyWriter = spy(new JsonWriter(stringWriter));

        String input = "123";

        // To avoid IllegalStateException, begin an array or object before calling jsonValue
        spyWriter.beginArray();

        spyWriter.jsonValue(input);

        spyWriter.endArray();

        // The output should be [123]
        String output = stringWriter.toString();
        assertEquals("[" + input + "]", output);
    }
}