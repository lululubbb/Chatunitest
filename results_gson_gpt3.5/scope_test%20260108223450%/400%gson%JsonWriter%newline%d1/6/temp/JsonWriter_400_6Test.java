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
import org.mockito.InOrder;

class JsonWriterNewlineTest {

    private Writer mockWriter;
    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() throws Exception {
        mockWriter = mock(Writer.class);
        jsonWriter = new JsonWriter(mockWriter);

        // Initialize stackSize to 0 (default empty state) via reflection
        setField(jsonWriter, "stackSize", 0);
    }

    @Test
    @Timeout(8000)
    public void testNewline_indentIsNull_noWrite() throws Exception {
        // indent is null by default
        Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
        newlineMethod.setAccessible(true);

        newlineMethod.invoke(jsonWriter);

        verifyNoInteractions(mockWriter);
    }

    @Test
    @Timeout(8000)
    public void testNewline_indentSet_stackSize1_writesNewlineOnly() throws Exception {
        // set indent
        jsonWriter.setIndent("  ");

        // set stackSize to 1 via reflection
        setField(jsonWriter, "stackSize", 1);

        Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
        newlineMethod.setAccessible(true);

        newlineMethod.invoke(jsonWriter);

        InOrder inOrder = inOrder(mockWriter);
        inOrder.verify(mockWriter).write('\n');
        // Since stackSize=1, loop does not run, so no indent writes
        verify(mockWriter, times(1)).write('\n');
        verifyNoMoreInteractions(mockWriter);
    }

    @Test
    @Timeout(8000)
    public void testNewline_indentSet_stackSize3_writesNewlineAndIndents() throws Exception {
        jsonWriter.setIndent("  ");
        setField(jsonWriter, "stackSize", 3);

        Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
        newlineMethod.setAccessible(true);

        newlineMethod.invoke(jsonWriter);

        InOrder inOrder = inOrder(mockWriter);
        inOrder.verify(mockWriter).write('\n');
        // loop from i=1 to size-1 (3-1=2), so two indent writes
        inOrder.verify(mockWriter).write("  ");
        inOrder.verify(mockWriter).write("  ");
        verifyNoMoreInteractions(mockWriter);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}