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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class JsonWriter_378_6Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    // Helper method to invoke private writeDeferredName via reflection
    private void verifyWriteDeferredNameCalled(JsonWriter spy) throws Exception {
        // Since writeDeferredName is private, we cannot verify it directly via Mockito.
        // Instead, we can override the spy with a custom spy class or check side effects.
        // Here, we check that writeDeferredName was called by spying on the spy's beginObject method
        // and using a custom spy that overrides writeDeferredName.
        // But since it's complicated, we skip direct verify and rely on indirect effects.
        // So this method is a placeholder if needed.
    }

    // Helper method to call private writeDeferredName to simulate verification
    private void callWriteDeferredName(JsonWriter writer) throws Exception {
        Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
        writeDeferredNameMethod.setAccessible(true);
        writeDeferredNameMethod.invoke(writer);
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_Normal() throws IOException {
        JsonWriter writerSpy = Mockito.spy(jsonWriter);

        try {
            // push initial state EMPTY_DOCUMENT to stack
            Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
            pushMethod.setAccessible(true);
            pushMethod.invoke(writerSpy, JsonScope.EMPTY_DOCUMENT);

            // set stackSize to 1 (after push)
            Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
            stackSizeField.setAccessible(true);
            stackSizeField.setInt(writerSpy, 1);

            // set deferredName to null to test writeDeferredName path where no deferredName is written
            Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
            deferredNameField.setAccessible(true);
            deferredNameField.set(writerSpy, null);

            JsonWriter result = writerSpy.beginObject();

            assertSame(writerSpy, result);

            // Since writeDeferredName is private, we cannot verify it directly.
            // Instead, check side effect: output should contain '{'
            String output = stringWriter.toString();
            assertTrue(output.contains("{"), "Output should contain '{'");

            // Check that stack top is EMPTY_OBJECT (code 3)
            Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
            peekMethod.setAccessible(true);
            int top = (int) peekMethod.invoke(writerSpy);
            assertEquals(JsonScope.EMPTY_OBJECT, top);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            fail("Reflection error: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_WithDeferredName() throws IOException {
        JsonWriter writerSpy = Mockito.spy(jsonWriter);

        try {
            // push initial state EMPTY_DOCUMENT to stack (not EMPTY_OBJECT)
            Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
            pushMethod.setAccessible(true);
            pushMethod.invoke(writerSpy, JsonScope.EMPTY_DOCUMENT);

            // set stackSize to 1 (after push)
            Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
            stackSizeField.setAccessible(true);
            stackSizeField.setInt(writerSpy, 1);

            // Set deferredName to a non-null value to test writeDeferredName writes name
            Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
            deferredNameField.setAccessible(true);
            deferredNameField.set(writerSpy, "myName");

            // Call beginObject, which calls writeDeferredName and open
            JsonWriter result = writerSpy.beginObject();

            assertSame(writerSpy, result);

            // Check output contains the deferred name quoted and colon (name should be written)
            String output = stringWriter.toString();
            assertTrue(output.contains("\"myName\""), "Output should contain deferred name quoted");
            assertTrue(output.contains(":"), "Output should contain colon after name");
            assertTrue(output.contains("{"), "Output should contain '{'");

            // Check deferredName is reset to null after writeDeferredName
            Object deferredNameAfter = deferredNameField.get(writerSpy);
            assertNull(deferredNameAfter, "deferredName should be null after writeDeferredName");

            // Check that stack top is EMPTY_OBJECT (code 3)
            Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
            peekMethod.setAccessible(true);
            int top = (int) peekMethod.invoke(writerSpy);
            assertEquals(JsonScope.EMPTY_OBJECT, top);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            fail("Reflection error: " + e.getMessage());
        }
    }

    // Subclass to override open to throw IOException for testing exception propagation
    static class JsonWriterWithOpenException extends JsonWriter {
        public JsonWriterWithOpenException(StringWriter out) {
            super(out);
        }

        // Remove @Override annotation because open is private in superclass and cannot be overridden
        @SuppressWarnings("unused")
        private JsonWriter open(int empty, char openBracket) throws IOException {
            throw new IOException("Mocked IOException from open");
        }
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_ThrowsIOExceptionFromOpen() {
        StringWriter sw = new StringWriter();
        JsonWriterWithOpenException writer = new JsonWriterWithOpenException(sw);

        try {
            // Use reflection to push initial state EMPTY_DOCUMENT to stack
            Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
            pushMethod.setAccessible(true);
            try {
                pushMethod.invoke(writer, JsonScope.EMPTY_DOCUMENT);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                throw e;
            }

            Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
            stackSizeField.setAccessible(true);
            stackSizeField.setInt(writer, 1);

            // Set deferredName to null to avoid interference
            Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
            deferredNameField.setAccessible(true);
            deferredNameField.set(writer, null);

            // Call beginObject via reflection to invoke the subclass's open method
            Method beginObjectMethod = JsonWriter.class.getDeclaredMethod("beginObject");
            beginObjectMethod.setAccessible(true);

            IOException thrown = assertThrows(IOException.class, () -> {
                try {
                    beginObjectMethod.invoke(writer);
                } catch (InvocationTargetException e) {
                    // unwrap the IOException thrown by open()
                    Throwable cause = e.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw e;
                }
            });

            assertEquals("Mocked IOException from open", thrown.getMessage());

        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            fail("Reflection error: " + e.getMessage());
        } catch (InvocationTargetException e) {
            fail("InvocationTargetException: " + e.getCause());
        }
    }

    @Test
    @Timeout(8000)
    public void testBeginObject_Integration() throws IOException {
        // Integration test without spy to check normal usage
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.setIndent("  ");

        // Begin object should write '{'
        JsonWriter result = writer.beginObject();
        assertSame(writer, result);
        String output = stringWriter.toString();
        assertTrue(output.contains("{"));
    }
}