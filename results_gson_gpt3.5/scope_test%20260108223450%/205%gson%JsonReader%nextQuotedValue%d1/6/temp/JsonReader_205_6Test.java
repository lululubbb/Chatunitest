package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_205_6Test {

    private JsonReader jsonReader;

    @BeforeEach
    public void setUp() throws IOException {
        // Create a Reader that will not be used directly since we will manipulate buffer and pos directly
        jsonReader = new JsonReader(new java.io.StringReader(""));
        // Reset internal fields via reflection if needed
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 0);
        setField(jsonReader, "lineNumber", 0);
        setField(jsonReader, "lineStart", 0);
    }

    @Test
    @Timeout(8000)
    public void testNextQuotedValue_simpleString() throws Throwable {
        // Setup buffer with a quoted string: "hello"
        char quote = '"';
        char[] buffer = new char[] { 'h', 'e', 'l', 'l', 'o', '"' };
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", buffer.length);

        String result = invokeNextQuotedValue(jsonReader, quote);

        assertEquals("hello", result);
        assertEquals(buffer.length, getField(jsonReader, "pos"));
    }

    @Test
    @Timeout(8000)
    public void testNextQuotedValue_withEscapeCharacter() throws Throwable {
        // Setup buffer with a quoted string containing an escape: "hel\nlo"
        char quote = '"';
        // buffer content: hel\nlo"
        char[] buffer = new char[] { 'h', 'e', 'l', '\\', 'n', 'l', 'o', '"' };
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", buffer.length);

        // Mock readEscapeCharacter to return '\n' when called
        JsonReader spyReader = spy(jsonReader);
        doReturn('\n').when(spyReader).readEscapeCharacter();

        String result = invokeNextQuotedValue(spyReader, quote);

        assertEquals("hel\nlo", result);
        assertEquals(buffer.length, getField(spyReader, "pos"));
    }

    @Test
    @Timeout(8000)
    public void testNextQuotedValue_withNewlineInString() throws Throwable {
        // Setup buffer with a quoted string containing a newline: "hel\nlo"
        char quote = '"';
        // buffer content: hel\nlo\n"
        char[] buffer = new char[] { 'h', 'e', 'l', '\n', 'l', 'o', '"' };
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", buffer.length);
        setField(jsonReader, "lineNumber", 0);
        setField(jsonReader, "lineStart", 0);

        String result = invokeNextQuotedValue(jsonReader, quote);

        assertEquals("hel\nlo", result);
        assertEquals(buffer.length, getField(jsonReader, "pos"));
        assertEquals(1, getField(jsonReader, "lineNumber"));
        assertEquals(4, getField(jsonReader, "lineStart"));
    }

    @Test
    @Timeout(8000)
    public void testNextQuotedValue_multibufferFill() throws Throwable {
        // Simulate a string that requires fillBuffer to be called
        char quote = '"';
        // buffer initially contains partial string without closing quote
        char[] buffer = new char[] { 'h', 'e', 'l', 'l', 'o' };
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", buffer.length);

        JsonReader spyReader = spy(jsonReader);

        // fillBuffer will be called once and will append a quote character to complete the string
        doAnswer(invocation -> {
            // Expand buffer with quote character at the end
            char[] oldBuffer = getField(spyReader, "buffer");
            char[] newBuffer = new char[oldBuffer.length + 1];
            System.arraycopy(oldBuffer, 0, newBuffer, 0, oldBuffer.length);
            newBuffer[oldBuffer.length] = quote;
            setField(spyReader, "buffer", newBuffer);
            setField(spyReader, "limit", newBuffer.length);
            return true;
        }).when(spyReader).fillBuffer(1);

        String result = invokeNextQuotedValue(spyReader, quote);

        assertEquals("hello", result);
        assertEquals(getField(spyReader, "limit"), getField(spyReader, "pos"));
    }

    @Test
    @Timeout(8000)
    public void testNextQuotedValue_unterminatedString_throws() throws Throwable {
        char quote = '"';
        char[] buffer = new char[] { 'h', 'e', 'l', 'l', 'o' };
        setField(jsonReader, "buffer", buffer);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", buffer.length);

        JsonReader spyReader = spy(jsonReader);
        doReturn(false).when(spyReader).fillBuffer(1);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            invokeNextQuotedValue(spyReader, quote);
        });
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof IOException);
        assertTrue(cause.getMessage().contains("Unterminated string"));
    }

    // Helper method to invoke private nextQuotedValue(char)
    private String invokeNextQuotedValue(JsonReader instance, char quote) throws Throwable {
        Method method = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
        method.setAccessible(true);
        try {
            return (String) method.invoke(instance, quote);
        } catch (InvocationTargetException e) {
            throw e;
        }
    }

    // Helper method to set private field via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to get private field via reflection
    @SuppressWarnings("unchecked")
    private <T> T getField(Object target, String fieldName) {
        try {
            java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}