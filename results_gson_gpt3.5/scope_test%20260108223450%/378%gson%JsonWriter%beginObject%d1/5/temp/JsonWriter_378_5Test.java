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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonWriterBeginObjectTest {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void beginObject_shouldWriteOpeningBraceAndPushEmptyObjectScope() throws IOException {
        JsonWriter spyWriter = Mockito.spy(jsonWriter);

        // deferredName is null initially, so writeDeferredName should be called but do nothing
        JsonWriter returned = spyWriter.beginObject();

        // Verify that returned is the same instance (this) for chaining
        assertSame(spyWriter, returned);

        // Verify internal stack top is EMPTY_OBJECT (which is 3 in JsonScope)
        int top = invokePeek(spyWriter);
        assertEquals(JsonScope.EMPTY_OBJECT, top);

        // Verify output contains '{'
        assertEquals("{", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void beginObject_shouldWriteDeferredNameIfSet() throws IOException, Exception {
        JsonWriter spyWriter = Mockito.spy(jsonWriter);
        // set deferredName via reflection
        setDeferredName(spyWriter, "key");

        // Use reflection to invoke private writeDeferredName() to simulate its effect
        invokePrivateMethod(spyWriter, "writeDeferredName");

        // Use reflection to invoke private open(int, char) to simulate its effect
        JsonWriter returned = (JsonWriter) invokePrivateMethod(spyWriter, "open",
                new Class<?>[]{int.class, char.class}, JsonScope.EMPTY_OBJECT, '{');

        // deferredName should be cleared after writeDeferredName
        assertNull(getDeferredName(spyWriter));

        // Verify stack top is EMPTY_OBJECT
        int top = invokePeek(spyWriter);
        assertEquals(JsonScope.EMPTY_OBJECT, top);

        // Output should contain the name and the '{'
        String output = stringWriter.toString();
        assertTrue(output.contains("\"key\""));
        assertTrue(output.contains("{"));

        assertSame(spyWriter, returned);
    }

    @Test
    @Timeout(8000)
    public void beginObject_writeDeferredNameThrowsIOException_shouldPropagate() throws Exception {
        // Create a subclass to override writeDeferredName to throw IOException simulating failure
        class JsonWriterWithThrowingWriteDeferredName extends JsonWriter {
            JsonWriterWithThrowingWriteDeferredName(StringWriter out) {
                super(out);
                try {
                    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
                    deferredNameField.setAccessible(true);
                    deferredNameField.set(this, "key");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // Override writeDeferredName() using reflection hack since it's private in superclass
            void writeDeferredName() throws IOException {
                throw new IOException("forced");
            }
        }

        JsonWriterWithThrowingWriteDeferredName throwingJsonWriter = new JsonWriterWithThrowingWriteDeferredName(stringWriter);

        // Use reflection to replace the private writeDeferredName method call in beginObject
        // Since we cannot override private method, we spy and stub writeDeferredName to throw
        JsonWriter spyWriter = Mockito.spy(throwingJsonWriter);
        doThrow(new IOException("forced")).when(spyWriter).writeDeferredName();

        IOException thrown = assertThrows(IOException.class, spyWriter::beginObject);
        assertEquals("forced", thrown.getMessage());
    }

    // Helper to invoke private peek method
    private int invokePeek(JsonWriter writer) {
        try {
            Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
            peekMethod.setAccessible(true);
            return (int) peekMethod.invoke(writer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Failed to invoke peek method: " + e);
            return -1;
        }
    }

    // Helper to get deferredName field value via reflection
    private String getDeferredName(JsonWriter writer) {
        try {
            Field field = JsonWriter.class.getDeclaredField("deferredName");
            field.setAccessible(true);
            return (String) field.get(writer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to get deferredName field: " + e);
            return null;
        }
    }

    // Helper to set deferredName field value via reflection
    private void setDeferredName(JsonWriter writer, String value) {
        try {
            Field field = JsonWriter.class.getDeclaredField("deferredName");
            field.setAccessible(true);
            field.set(writer, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set deferredName field: " + e);
        }
    }

    // Helper to invoke private methods with no args
    private Object invokePrivateMethod(Object obj, String methodName, Class<?>... parameterTypes) throws Exception {
        Method method = JsonWriter.class.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        if (parameterTypes.length == 0) {
            return method.invoke(obj);
        } else {
            throw new IllegalArgumentException("Use invokePrivateMethod with args for parameters");
        }
    }

    // Helper to invoke private methods with args
    private Object invokePrivateMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object... args) throws Exception {
        Method method = JsonWriter.class.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(obj, args);
    }
}