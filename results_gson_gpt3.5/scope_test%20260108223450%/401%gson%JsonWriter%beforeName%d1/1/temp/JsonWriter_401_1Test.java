package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterBeforeNameTest {

    private JsonWriter jsonWriter;
    private Writer mockWriter;

    @BeforeEach
    void setUp() {
        mockWriter = Mockito.mock(Writer.class);
        jsonWriter = new JsonWriter(mockWriter);
    }

    private void setStackAndStackSize(int[] stackValues, int stackSize) throws Exception {
        var stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(jsonWriter, stackValues);
        var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, stackSize);
    }

    private int peekContext() throws Exception {
        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        return (int) peekMethod.invoke(jsonWriter);
    }

    private void invokeBeforeName() throws Throwable {
        Method beforeNameMethod = JsonWriter.class.getDeclaredMethod("beforeName");
        beforeNameMethod.setAccessible(true);
        try {
            beforeNameMethod.invoke(jsonWriter);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void beforeName_writesComma_whenContextIsNonEmptyObject() throws Throwable {
        // Arrange
        int[] stack = new int[32];
        stack[0] = NONEMPTY_OBJECT;
        setStackAndStackSize(stack, 1);

        // Act
        invokeBeforeName();

        // Assert
        Mockito.verify(mockWriter).write(',');
        int contextAfter = peekContext();
        assertEquals(DANGLING_NAME, contextAfter);
    }

    @Test
    @Timeout(8000)
    void beforeName_newlineIsCalled_andReplacesTop_whenContextIsEmptyObject() throws Throwable {
        // Arrange
        int[] stack = new int[32];
        stack[0] = EMPTY_OBJECT;
        setStackAndStackSize(stack, 1);

        // Act
        invokeBeforeName();

        // Assert
        Mockito.verify(mockWriter, Mockito.never()).write(',');
        int contextAfter = peekContext();
        assertEquals(DANGLING_NAME, contextAfter);
    }

    @Test
    @Timeout(8000)
    void beforeName_throwsIllegalStateException_whenContextIsNotObject() throws Throwable {
        // Arrange
        int[] stack = new int[32];
        stack[0] = 999; // some invalid context not EMPTY_OBJECT or NONEMPTY_OBJECT
        setStackAndStackSize(stack, 1);

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class, this::invokeBeforeName);
        assertEquals("Nesting problem.", thrown.getMessage());
    }
}