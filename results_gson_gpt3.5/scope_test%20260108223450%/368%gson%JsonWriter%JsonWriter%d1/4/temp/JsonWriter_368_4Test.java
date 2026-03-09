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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_368_4Test {

    private StringWriter stringWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void constructor_nullWriter_throwsNullPointerException() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new JsonWriter(null));
        assertEquals("out == null", npe.getMessage());
    }

    @Test
    @Timeout(8000)
    public void constructor_validWriter_initializesStack() throws Exception {
        // Using reflection to check private fields
        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonWriter);
        assertNotNull(stack);
        assertEquals(32, stack.length);

        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        int stackSize = (int) stackSizeField.get(jsonWriter);
        // The initial stackSize is 1, not 0, because constructor pushes EMPTY_DOCUMENT state
        assertEquals(1, stackSize);
    }

    @Test
    @Timeout(8000)
    public void testPrivateStringMethod_escapesCorrectly() throws Exception {
        Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
        stringMethod.setAccessible(true);

        jsonWriter.beginObject();
        stringMethod.invoke(jsonWriter, "abc\"\\/\b\f\n\r\t\u2028\u2029");
        jsonWriter.endObject();
        jsonWriter.flush();

        String result = stringWriter.toString();
        // The string method writes a string value, so output should be {"abc\"\\/\b\f\n\r\t\u2028\u2029"}
        // with proper escapes inside quotes
        assertTrue(result.startsWith("{\""));
        assertTrue(result.endsWith("\"}"));

        // Check some escape sequences
        assertTrue(result.contains("\\\""));
        assertTrue(result.contains("\\\\"));
        assertTrue(result.contains("\\/"));
        assertTrue(result.contains("\\b"));
        assertTrue(result.contains("\\f"));
        assertTrue(result.contains("\\n"));
        assertTrue(result.contains("\\r"));
        assertTrue(result.contains("\\t"));
        // Unicode line separator \u2028 and paragraph separator \u2029 should be escaped as \u2028 and \u2029
        assertTrue(result.contains("\\u2028"));
        assertTrue(result.contains("\\u2029"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateNewlineMethod_writesIndentAndNewline() throws Exception {
        jsonWriter.setIndent("  ");
        jsonWriter.beginArray();
        Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
        newlineMethod.setAccessible(true);

        // Push some states onto stack to simulate depth
        Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
        pushMethod.setAccessible(true);
        pushMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT);
        pushMethod.invoke(jsonWriter, JsonScope.NONEMPTY_OBJECT);

        newlineMethod.invoke(jsonWriter);
        jsonWriter.flush();

        String output = stringWriter.toString();
        // It should contain a newline and two indents (4 spaces)
        assertTrue(output.contains("\n"));
        assertTrue(output.contains("    "));
    }

    @Test
    @Timeout(8000)
    public void testPrivateBeforeName_andBeforeValue_methods() throws Exception {
        Method beforeName = JsonWriter.class.getDeclaredMethod("beforeName");
        Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
        beforeName.setAccessible(true);
        beforeValue.setAccessible(true);

        jsonWriter.beginObject();
        // Remove deferredName to avoid duplicate colon and comma
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, null);

        beforeName.invoke(jsonWriter);
        jsonWriter.name("key");
        beforeValue.invoke(jsonWriter);
        jsonWriter.value("value");
        jsonWriter.endObject();
        jsonWriter.flush();

        String output = stringWriter.toString();
        assertEquals("{\"key\":\"value\"}", output);
    }

    @Test
    @Timeout(8000)
    public void testOpen_andClose_methods() throws Exception {
        Method open = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
        Method close = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        open.setAccessible(true);
        close.setAccessible(true);

        // open with EMPTY_ARRAY and '['
        Object retOpen = open.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, '[');
        assertSame(jsonWriter, retOpen);

        // close with EMPTY_ARRAY, NONEMPTY_ARRAY and ']'
        Object retClose = close.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
        assertSame(jsonWriter, retClose);
    }

    @Test
    @Timeout(8000)
    public void testPush_peek_replaceTop_methods() throws Exception {
        Method push = JsonWriter.class.getDeclaredMethod("push", int.class);
        Method peek = JsonWriter.class.getDeclaredMethod("peek");
        Method replaceTop = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
        push.setAccessible(true);
        peek.setAccessible(true);
        replaceTop.setAccessible(true);

        push.invoke(jsonWriter, JsonScope.EMPTY_DOCUMENT);
        int peeked = (int) peek.invoke(jsonWriter);
        assertEquals(JsonScope.EMPTY_DOCUMENT, peeked);

        replaceTop.invoke(jsonWriter, JsonScope.NONEMPTY_DOCUMENT);
        int replaced = (int) peek.invoke(jsonWriter);
        assertEquals(JsonScope.NONEMPTY_DOCUMENT, replaced);
    }

    @Test
    @Timeout(8000)
    public void testName_deferredNameBehavior() throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("a");
        jsonWriter.value("b");
        jsonWriter.name("c");
        jsonWriter.nullValue();
        jsonWriter.endObject();
        jsonWriter.flush();

        String output = stringWriter.toString();
        assertEquals("{\"a\":\"b\",\"c\":null}", output);
    }

    @Test
    @Timeout(8000)
    public void testValue_methods() throws IOException {
        jsonWriter.beginArray();
        jsonWriter.value("string");
        jsonWriter.value(true);
        jsonWriter.value(Boolean.FALSE);
        jsonWriter.value(123L);
        jsonWriter.value(1.23f);
        jsonWriter.value(4.56d);
        jsonWriter.value((Number) 789);
        jsonWriter.nullValue();
        jsonWriter.endArray();
        jsonWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.startsWith("["));
        assertTrue(output.endsWith("]"));
        assertTrue(output.contains("\"string\""));
        assertTrue(output.contains("true"));
        assertTrue(output.contains("false"));
        assertTrue(output.contains("123"));
        assertTrue(output.contains("1.23"));
        assertTrue(output.contains("4.56"));
        assertTrue(output.contains("789"));
        assertTrue(output.contains("null"));
    }

    @Test
    @Timeout(8000)
    public void testJsonValue_acceptsRawJson() throws IOException {
        jsonWriter.beginArray();
        jsonWriter.jsonValue("{\"raw\":true}");
        jsonWriter.endArray();
        jsonWriter.flush();

        String output = stringWriter.toString();
        assertEquals("[{\"raw\":true}]", output);
    }

    @Test
    @Timeout(8000)
    public void testSettersAndGetters() throws Exception {
        jsonWriter.setIndent("  ");
        Field indentField = JsonWriter.class.getDeclaredField("indent");
        indentField.setAccessible(true);
        assertEquals("  ", indentField.get(jsonWriter));

        jsonWriter.setLenient(true);
        assertTrue(jsonWriter.isLenient());

        jsonWriter.setHtmlSafe(true);
        assertTrue(jsonWriter.isHtmlSafe());

        jsonWriter.setSerializeNulls(false);
        assertFalse(jsonWriter.getSerializeNulls());
    }

    @Test
    @Timeout(8000)
    public void testFlushAndClose() throws IOException {
        Writer mockWriter = mock(Writer.class);
        JsonWriter writer = new JsonWriter(mockWriter);

        // Begin and end a document properly to avoid Incomplete document exception on close
        writer.beginArray();
        writer.endArray();

        writer.flush();
        verify(mockWriter).flush();

        writer.close();
        verify(mockWriter).close();
    }
}