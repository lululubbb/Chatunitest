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
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.google.gson.stream.JsonWriter;

class JsonWriter_NewlineTest {

  private Writer out;
  private JsonWriter jsonWriter;
  private Method newlineMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    out = mock(Writer.class);
    jsonWriter = new JsonWriter(out);
    newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
    newlineMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void newline_indentNull_noWrite() throws IOException, InvocationTargetException, IllegalAccessException {
    // indent is null by default, so newline should do nothing
    newlineMethod.invoke(jsonWriter);
    verifyNoInteractions(out);
  }

  @Test
    @Timeout(8000)
  void newline_indentSet_writesNewlineAndIndent() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
    // Set indent to some string
    String indent = "  ";
    jsonWriter.setIndent(indent);

    // Use reflection to set stackSize to 1 (minimum)
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    // Invoke newline, expect a single newline written and no indent (since i=1, size=1, loop does not run)
    newlineMethod.invoke(jsonWriter);

    InOrder inOrder = inOrder(out);
    inOrder.verify(out).write('\n');
    verify(out, never()).write(indent);

    // Now set stackSize to 4, expect newline + indent written 3 times
    stackSizeField.setInt(jsonWriter, 4);

    // Reset mocks before second call
    reset(out);

    newlineMethod.invoke(jsonWriter);

    inOrder = inOrder(out);
    inOrder.verify(out).write('\n');
    inOrder.verify(out, times(3)).write(indent);
    verifyNoMoreInteractions(out);
  }

  @Test
    @Timeout(8000)
  void newline_indentEmptyString_writesNewlineNoIndent() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    jsonWriter.setIndent("");
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 5);

    newlineMethod.invoke(jsonWriter);

    InOrder inOrder = inOrder(out);
    inOrder.verify(out).write('\n');
    // indent is empty string, so write("") called 4 times but effectively no output
    inOrder.verify(out, times(4)).write("");
    verifyNoMoreInteractions(out);
  }

}