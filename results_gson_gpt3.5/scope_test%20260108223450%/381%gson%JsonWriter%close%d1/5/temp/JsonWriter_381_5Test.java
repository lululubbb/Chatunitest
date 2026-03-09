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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

class JsonWriterCloseTest {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void closeMethod_validEmptyContext_writesCloseBracketAndReturnsThis() throws Throwable {
        // Use reflection to access private close(int,int,char) method
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Prepare stack and stackSize to simulate empty context
        setStackAndStackSize(1, new int[]{JsonScope.EMPTY_ARRAY});

        // deferredName must be null
        setDeferredName(null);

        // Call close with empty=EMPTY_ARRAY, nonempty=NONEMPTY_ARRAY, closeBracket=']'
        Object returned = closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');

        // Verify returned object is jsonWriter itself
        assertSame(jsonWriter, returned);

        // Verify stackSize decremented
        int stackSize = getStackSize();
        assertEquals(0, stackSize);

        // Verify output contains closeBracket ']'
        assertTrue(stringWriter.toString().endsWith("]"));
    }

    @Test
    @Timeout(8000)
    void closeMethod_validNonEmptyContext_writesNewlineAndCloseBracket() throws Throwable {
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Setup stack with NONEMPTY_OBJECT context
        // stackSize must be 1 for peek() to return correct context
        JsonWriter spyWriter = spy(jsonWriter);

        setStackAndStackSize(spyWriter, 1, new int[]{JsonScope.NONEMPTY_OBJECT});
        setDeferredName(spyWriter, null);

        // Create a spy on the Writer to detect if newline() writes something
        Writer spyOut = spy(stringWriter);
        setOut(spyWriter, spyOut);

        Object returned = closeMethod.invoke(spyWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');

        assertSame(spyWriter, returned);
        int stackSize = getStackSize(spyWriter);
        assertEquals(0, stackSize);

        // Capture all write calls to spyOut
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(spyOut, atLeastOnce()).write(captor.capture());

        boolean newlineWritten = captor.getAllValues().stream().anyMatch(s -> s.contains("\n"));

        assertTrue(newlineWritten, "Expected newline() to write a newline character");

        // Verify output ends with '}'
        assertTrue(stringWriter.toString().endsWith("}"));
    }

    @Test
    @Timeout(8000)
    void closeMethod_invalidContext_throwsIllegalStateException() throws Throwable {
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        // Setup stack with context that is not empty or nonempty
        setStackAndStackSize(1, new int[]{JsonScope.EMPTY_DOCUMENT});

        setDeferredName(null);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            try {
                closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });

        assertEquals("Nesting problem.", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void closeMethod_withDeferredName_throwsIllegalStateException() throws Throwable {
        Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
        closeMethod.setAccessible(true);

        setStackAndStackSize(1, new int[]{JsonScope.EMPTY_OBJECT});

        setDeferredName("name");

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            try {
                closeMethod.invoke(jsonWriter, JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, '}');
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });

        assertEquals("Dangling name: name", thrown.getMessage());
    }

    // Helper methods to set private fields using reflection

    private void setStackAndStackSize(int stackSize, int[] stackContents) throws Exception {
        setStackAndStackSize(jsonWriter, stackSize, stackContents);
    }

    private void setStackAndStackSize(JsonWriter writer, int stackSize, int[] stackContents) throws Exception {
        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(writer, stackContents);

        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(writer, stackSize);
    }

    private int getStackSize() throws Exception {
        return getStackSize(jsonWriter);
    }

    private int getStackSize(JsonWriter writer) throws Exception {
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        return stackSizeField.getInt(writer);
    }

    private void setDeferredName(String name) throws Exception {
        setDeferredName(jsonWriter, name);
    }

    private void setDeferredName(JsonWriter writer, String name) throws Exception {
        var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(writer, name);
    }

    private void setOut(JsonWriter writer, Writer out) throws Exception {
        Field outField = JsonWriter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(writer, out);
    }
}