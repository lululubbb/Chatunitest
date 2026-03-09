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
  void newline_indentNull_noWrite() throws Throwable {
    // indent is null by default
    // invoke newline
    try {
      newlineMethod.invoke(jsonWriter);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
    // verify no interaction with writer
    verifyNoInteractions(mockWriter);
  }

  @Test
    @Timeout(8000)
  void newline_indentNonNull_writesNewlineAndIndent() throws Throwable {
    // set indent string
    jsonWriter.setIndent("  ");
    // set stackSize to 4 to test loop (write indent 3 times)
    setField(jsonWriter, "stackSize", 4);

    // invoke newline
    try {
      newlineMethod.invoke(jsonWriter);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    InOrder inOrder = inOrder(mockWriter);
    // verify newline char written once
    inOrder.verify(mockWriter).write('\n');
    // verify indent string written stackSize-1 times (3 times)
    inOrder.verify(mockWriter, times(3)).write("  ");
    inOrder.verifyNoMoreInteractions();
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      var field = JsonWriter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}