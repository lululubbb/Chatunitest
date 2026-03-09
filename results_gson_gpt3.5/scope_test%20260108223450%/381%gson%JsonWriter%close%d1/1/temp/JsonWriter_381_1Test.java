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
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterCloseTest {

    private Writer mockWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        mockWriter = mock(Writer.class);
        jsonWriter = new JsonWriter(mockWriter);
    }

    @Test
    @Timeout(8000)
    void close_withEmptyContext_writesCloseBracketWithoutNewline() throws Exception {
        // Arrange
        setStackAndStackSize(EMPTY_DOCUMENT);
        setDeferredName(null);

        // Act
        JsonWriter result = invokeClose(EMPTY_DOCUMENT, NONEMPTY_DOCUMENT, '}');

        // Assert
        assertSame(jsonWriter, result);
        verify(mockWriter).write('}');
        verify(mockWriter, never()).write("\n");
    }

    @Test
    @Timeout(8000)
    void close_withNonEmptyContext_writesCloseBracketWithNewline() throws Exception {
        // Arrange
        setStackAndStackSize(NONEMPTY_DOCUMENT);
        setDeferredName(null);

        // Spy on jsonWriter to intercept private newline() call
        JsonWriter spyWriter = spy(jsonWriter);

        // Act
        JsonWriter result = invokeClose(spyWriter, EMPTY_DOCUMENT, NONEMPTY_DOCUMENT, '}');

        // Assert
        assertSame(spyWriter, result);

        // Verify that newline() was called by verifying the writer wrote a newline
        verify(mockWriter).write("\n");
        verify(mockWriter).write('}');
    }

    @Test
    @Timeout(8000)
    void close_withDeferredName_throwsIllegalStateException() throws Exception {
        // Arrange
        setStackAndStackSize(EMPTY_DOCUMENT);
        setDeferredName("danglingName");

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> invokeClose(EMPTY_DOCUMENT, NONEMPTY_DOCUMENT, '}'));
        assertEquals("Dangling name: danglingName", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void close_withInvalidContext_throwsIllegalStateException() throws Exception {
        // Arrange
        setStackAndStackSize(EMPTY_OBJECT);
        setDeferredName(null);

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> invokeClose(EMPTY_DOCUMENT, NONEMPTY_DOCUMENT, '}'));
        assertEquals("Nesting problem.", ex.getMessage());
    }

    // Helper methods and constants for contexts from JsonScope
    private static final int EMPTY_DOCUMENT = 6;
    private static final int NONEMPTY_DOCUMENT = 7;
    private static final int EMPTY_OBJECT = 1;
    private static final int NONEMPTY_OBJECT = 2;

    private void setStackAndStackSize(int context) throws Exception {
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stack[0] = context;
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);
    }

    private void setDeferredName(String name) throws Exception {
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(jsonWriter, name);
    }

    private int peek() throws Exception {
        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        return (int) peekMethod.invoke(jsonWriter);
    }

    private JsonWriter invokeClose(int empty, int nonempty, char closeBracket) throws Exception {
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);
        return (JsonWriter) closeMethod.invoke(jsonWriter, empty, nonempty, closeBracket);
    }

    private JsonWriter invokeClose(JsonWriter instance, int empty, int nonempty, char closeBracket) throws Exception {
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);
        return (JsonWriter) closeMethod.invoke(instance, empty, nonempty, closeBracket);
    }
}