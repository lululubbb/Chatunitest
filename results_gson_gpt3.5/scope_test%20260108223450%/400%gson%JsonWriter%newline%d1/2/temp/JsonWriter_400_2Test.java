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

class JsonWriter_newline_Test {

  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  void newline_indentNull_doesNotWrite() throws Throwable {
    // indent is null by default
    invokeNewline(jsonWriter);
    verify(mockWriter, never()).write(anyInt());
    verify(mockWriter, never()).write(any(String.class));
  }

  @Test
    @Timeout(8000)
  void newline_indentEmpty_writesNewlineNoIndent() throws Throwable {
    jsonWriter.setIndent("");
    setStackSize(jsonWriter, 3);

    invokeNewline(jsonWriter);

    // Should write newline once, no indent strings because indent is empty
    verify(mockWriter).write('\n');
    verify(mockWriter, never()).write("");
  }

  @Test
    @Timeout(8000)
  void newline_indentNonEmpty_writesNewlineAndIndents() throws Throwable {
    jsonWriter.setIndent("  ");
    setStackSize(jsonWriter, 4);

    invokeNewline(jsonWriter);

    InOrder inOrder = inOrder(mockWriter);
    inOrder.verify(mockWriter).write('\n');
    // indent string "  " written stackSize-1 times = 3 times
    inOrder.verify(mockWriter).write("  ");
    inOrder.verify(mockWriter).write("  ");
    inOrder.verify(mockWriter).write("  ");
    verifyNoMoreInteractions(mockWriter);
  }

  // Helper to set private stackSize field via reflection
  private static void setStackSize(JsonWriter writer, int size) throws Exception {
    var field = JsonWriter.class.getDeclaredField("stackSize");
    field.setAccessible(true);
    field.setInt(writer, size);
  }

  // Helper to invoke private void newline() throws IOException via reflection
  private static void invokeNewline(JsonWriter writer) throws Throwable {
    Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
    newlineMethod.setAccessible(true);
    try {
      newlineMethod.invoke(writer);
    } catch (InvocationTargetException e) {
      // unwrap IOException or rethrow
      throw e.getCause();
    }
  }
}