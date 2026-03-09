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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonWriter_newline_Test {

    private Writer mockWriter;
    private JsonWriter jsonWriter;
    private Method newlineMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        mockWriter = mock(Writer.class);
        jsonWriter = new JsonWriter(mockWriter);
        newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
        newlineMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void newline_indentIsNull_noWrite() throws IOException, InvocationTargetException, IllegalAccessException {
        // indent is null by default, so newline should do nothing.
        // Just invoke and verify no interactions with writer.
        newlineMethod.invoke(jsonWriter);
        verifyNoInteractions(mockWriter);
    }

    @Test
    @Timeout(8000)
    void newline_indentNotNull_writesNewlineAndIndent() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Set indent to a string
        String indentValue = "  ";
        // Use reflection to set private field indent
        Field indentField = JsonWriter.class.getDeclaredField("indent");
        indentField.setAccessible(true);
        indentField.set(jsonWriter, indentValue);

        // Set stackSize to 4 (so loop from i=1 to 3)
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 4);

        // Invoke newline
        newlineMethod.invoke(jsonWriter);

        // Verify write('\n') called once
        verify(mockWriter).write('\n');

        // Verify indent string written 3 times (i=1 to 3)
        verify(mockWriter, times(3)).write(indentValue);

        // Verify no more interactions
        verifyNoMoreInteractions(mockWriter);
    }

    @Test
    @Timeout(8000)
    void newline_indentEmptyString_writesNewlineOnly() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // indent empty string
        Field indentField = JsonWriter.class.getDeclaredField("indent");
        indentField.setAccessible(true);
        indentField.set(jsonWriter, "");

        // stackSize 5 (loop 1 to 4)
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 5);

        newlineMethod.invoke(jsonWriter);

        verify(mockWriter).write('\n');
        // indent is empty string, so write("") called 4 times
        verify(mockWriter, times(4)).write("");
        verifyNoMoreInteractions(mockWriter);
    }

    @Test
    @Timeout(8000)
    void newline_stackSizeOne_writesOnlyNewline() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Field indentField = JsonWriter.class.getDeclaredField("indent");
        indentField.setAccessible(true);
        indentField.set(jsonWriter, "  ");

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        // loop from i=1 to size(1) => i<1 no iteration, so only newline written
        newlineMethod.invoke(jsonWriter);

        verify(mockWriter).write('\n');
        verifyNoMoreInteractions(mockWriter);
    }
}