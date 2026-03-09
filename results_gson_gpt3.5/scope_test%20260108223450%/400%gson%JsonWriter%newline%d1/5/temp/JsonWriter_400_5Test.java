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

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

class JsonWriterNewlineTest {

    private JsonWriter jsonWriter;
    private Writer mockWriter;

    private Method newlineMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        mockWriter = mock(Writer.class);
        jsonWriter = new JsonWriter(mockWriter);

        // Access private newline() method via reflection
        newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
        newlineMethod.setAccessible(true);

        // Set stackSize to 1 initially to avoid unexpected behavior
        setPrivateField(jsonWriter, "stackSize", 1);
    }

    @Test
    @Timeout(8000)
    public void testNewline_IndentNull_NoWrite() throws IOException, InvocationTargetException, IllegalAccessException {
        // indent is null by default, so newline should do nothing
        newlineMethod.invoke(jsonWriter);
        // Verify no write calls to the writer
        verify(mockWriter, never()).write(anyInt());
        verify(mockWriter, never()).write(anyString());
    }

    @Test
    @Timeout(8000)
    public void testNewline_WithIndent_WritesNewlineAndIndent() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Set indent to some string
        String indent = "  ";
        jsonWriter.setIndent(indent);

        // Set stackSize to 1: loop will not execute (i=1, size=1, i<size false)
        setPrivateField(jsonWriter, "stackSize", 1);
        newlineMethod.invoke(jsonWriter);
        // Should write newline once, no indent strings
        verify(mockWriter).write('\n');
        verify(mockWriter, never()).write(indent);

        reset(mockWriter);

        // Set stackSize to 3: loop executes for i=1,2
        setPrivateField(jsonWriter, "stackSize", 3);
        newlineMethod.invoke(jsonWriter);
        // Should write newline once
        verify(mockWriter).write('\n');
        // Should write indent twice
        verify(mockWriter, times(2)).write(indent);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}