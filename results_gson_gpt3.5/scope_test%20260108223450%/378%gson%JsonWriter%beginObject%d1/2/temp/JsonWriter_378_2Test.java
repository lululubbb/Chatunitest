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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterBeginObjectTest {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void beginObject_writesOpeningBraceAndPushesEmptyObject() throws IOException {
        JsonWriter spyWriter = jsonWriterSpy();

        // invoke writeDeferredName() via reflection, do nothing
        doNothingWriteDeferredName(spyWriter);

        JsonWriter returned = spyWriter.beginObject();

        // returned should be the same instance
        assertSame(spyWriter, returned);

        // The stack should have one element: EMPTY_OBJECT (which is 3)
        // We access private stack and stackSize via reflection
        try {
            Field stackField = JsonWriter.class.getDeclaredField("stack");
            stackField.setAccessible(true);
            int[] stack = (int[]) stackField.get(spyWriter);

            Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
            stackSizeField.setAccessible(true);
            int stackSize = (int) stackSizeField.get(spyWriter);

            assertEquals(1, stackSize);
            assertEquals(JsonScope.EMPTY_OBJECT, stack[stackSize - 1]);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }

        // The output should contain '{'
        assertTrue(stringWriter.toString().contains("{"));
    }

    @Test
    @Timeout(8000)
    void beginObject_callsWriteDeferredName() throws Exception {
        JsonWriter spyWriter = jsonWriterSpy();

        // Arrange: deferredName is null, so writeDeferredName writes nothing
        doNothingWriteDeferredName(spyWriter);

        spyWriter.beginObject();

        // verify that writeDeferredName was called once via reflection invocation count
        int callCount = getWriteDeferredNameCallCount(spyWriter);
        assertEquals(1, callCount);
    }

    @Test
    @Timeout(8000)
    void beginObject_withDeferredName_invokesWriteDeferredNameAndOpensObject() throws Exception {
        JsonWriter spyWriter = jsonWriterSpy();

        // Set deferredName via reflection
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(spyWriter, "name");

        // Replace writeDeferredName with reflective invocation that clears deferredName and writes the name
        setWriteDeferredNameHandler(spyWriter, () -> {
            try {
                deferredNameField.set(spyWriter, null);
                Field outField = JsonWriter.class.getDeclaredField("out");
                outField.setAccessible(true);
                Writer out = (Writer) outField.get(spyWriter);
                out.write("\"name\":");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        JsonWriter returned = spyWriter.beginObject();

        // deferredName should be cleared
        assertNull(deferredNameField.get(spyWriter));

        // output should contain the name and opening brace
        String output = stringWriter.toString();
        assertTrue(output.contains("\"name\":"));
        assertTrue(output.contains("{"));

        assertSame(spyWriter, returned);
    }

    @Test
    @Timeout(8000)
    void beginObject_stackResizesWhenFull() throws Exception {
        JsonWriter writer = new JsonWriter(stringWriter);

        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);

        int[] stack = (int[]) stackField.get(writer);
        for (int i = 0; i < 32; i++) {
            stack[i] = JsonScope.EMPTY_ARRAY;
        }
        stackSizeField.setInt(writer, 32);

        JsonWriter spyWriter = jsonWriterSpy(writer);
        doNothingWriteDeferredName(spyWriter);

        spyWriter.beginObject();

        int[] newStack = (int[]) stackField.get(spyWriter);
        int stackSize = (int) stackSizeField.get(spyWriter);

        assertEquals(33, stackSize);
        assertEquals(JsonScope.EMPTY_OBJECT, newStack[stackSize - 1]);
        assertTrue(newStack.length > 32);
    }

    // Helpers to handle private writeDeferredName invocation and spying

    private JsonWriter jsonWriterSpy() {
        return jsonWriterSpy(jsonWriter);
    }

    private JsonWriter jsonWriterSpy(JsonWriter original) {
        return new JsonWriterProxy(original);
    }

    private void doNothingWriteDeferredName(JsonWriter writer) {
        ((JsonWriterProxy) writer).setWriteDeferredNameHandler(() -> {
            // do nothing
        });
    }

    private void setWriteDeferredNameHandler(JsonWriter writer, Runnable handler) {
        ((JsonWriterProxy) writer).setWriteDeferredNameHandler(handler);
    }

    private int getWriteDeferredNameCallCount(JsonWriter writer) {
        return ((JsonWriterProxy) writer).getWriteDeferredNameCallCount();
    }

    // Proxy subclass to override private writeDeferredName via reflection

    private static class JsonWriterProxy extends JsonWriter {

        private final JsonWriter delegate;
        private Runnable writeDeferredNameHandler = null;
        private int writeDeferredNameCallCount = 0;

        JsonWriterProxy(JsonWriter delegate) {
            super(getDelegateOut(delegate));
            this.delegate = delegate;
            try {
                copyState(delegate, this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        void setWriteDeferredNameHandler(Runnable handler) {
            this.writeDeferredNameHandler = handler;
        }

        int getWriteDeferredNameCallCount() {
            return writeDeferredNameCallCount;
        }

        @Override
        public JsonWriter beginObject() throws IOException {
            invokeWriteDeferredName();
            return open(JsonScope.EMPTY_OBJECT, '{');
        }

        private void invokeWriteDeferredName() throws IOException {
            writeDeferredNameCallCount++;
            if (writeDeferredNameHandler != null) {
                writeDeferredNameHandler.run();
            } else {
                // fallback: invoke original private method via reflection
                try {
                    Method m = JsonWriter.class.getDeclaredMethod("writeDeferredName");
                    m.setAccessible(true);
                    m.invoke(delegate);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        }

        // Remove @Override annotation since open is private in JsonWriter and cannot be overridden
        public JsonWriter open(int empty, char openBracket) throws IOException {
            try {
                Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
                openMethod.setAccessible(true);
                return (JsonWriter) openMethod.invoke(delegate, empty, openBracket);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

        // Override toString to reflect delegate's out content
        @Override
        public String toString() {
            return delegate.toString();
        }

        // Override other public methods to delegate if needed
        public String indent() {
            try {
                Field indentField = JsonWriter.class.getDeclaredField("indent");
                indentField.setAccessible(true);
                return (String) indentField.get(delegate);
            } catch (Exception e) {
                return null;
            }
        }

        // Static helper to get the Writer out field from delegate
        private static Writer getDelegateOut(JsonWriter delegate) {
            try {
                Field outField = JsonWriter.class.getDeclaredField("out");
                outField.setAccessible(true);
                return (Writer) outField.get(delegate);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // Copy private fields from original to proxy instance
        private static void copyState(JsonWriter from, JsonWriterProxy to) throws Exception {
            Field[] fields = JsonWriter.class.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Object value = f.get(from);
                f.set(to, value);
            }
        }
    }
}