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

class JsonWriterOpenTest {
  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  public void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void testOpen_emptyArray() throws Throwable {
    invokeOpen(JsonScope.EMPTY_ARRAY, '[');
    verify(mockWriter).write('[');
  }

  @Test
    @Timeout(8000)
  public void testOpen_emptyObject() throws Throwable {
    invokeOpen(JsonScope.EMPTY_OBJECT, '{');
    verify(mockWriter).write('{');
  }

  @Test
    @Timeout(8000)
  public void testOpen_emptyDocument() throws Throwable {
    invokeOpen(JsonScope.EMPTY_DOCUMENT, '{');
    verify(mockWriter).write('{');
  }

  @Test
    @Timeout(8000)
  public void testOpen_nonEmptyArray() throws Throwable {
    invokeOpen(JsonScope.NONEMPTY_ARRAY, '[');
    verify(mockWriter).write('[');
  }

  @Test
    @Timeout(8000)
  public void testOpen_nonEmptyObject() throws Throwable {
    invokeOpen(JsonScope.NONEMPTY_OBJECT, '{');
    verify(mockWriter).write('{');
  }

  @Test
    @Timeout(8000)
  public void testOpen_nonEmptyDocument() throws Throwable {
    invokeOpen(JsonScope.NONEMPTY_DOCUMENT, '{');
    verify(mockWriter).write('{');
  }

  @Test
    @Timeout(8000)
  public void testOpen_danglingName() throws Throwable {
    // Setup stack to DANGLING_NAME to verify beforeValue behavior
    setStack(new int[] {JsonScope.DANGLING_NAME}, 1);
    invokeOpen(JsonScope.EMPTY_ARRAY, '[');
    verify(mockWriter).write('[');
  }

  @Test
    @Timeout(8000)
  public void testOpen_throwsIOException() throws Throwable {
    doThrow(new IOException("write error")).when(mockWriter).write('[');
    IOException thrown = assertThrows(IOException.class, () -> invokeOpen(JsonScope.EMPTY_ARRAY, '['));
    assertEquals("write error", thrown.getMessage());
  }

  // Helper method to invoke private open method via reflection
  private JsonWriter invokeOpen(int empty, char openBracket) throws Throwable {
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);
    try {
      return (JsonWriter) openMethod.invoke(jsonWriter, empty, openBracket);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  // Helper to set private stack and stackSize fields to test beforeValue branch coverage
  private void setStack(int[] stackValues, int size) throws Exception {
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(jsonWriter, stackValues);
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, size);
  }

  // JsonScope constants for testing
  private static class JsonScope {
    static final int EMPTY_ARRAY = 1;
    static final int EMPTY_OBJECT = 3;
    static final int EMPTY_DOCUMENT = 0;
    static final int NONEMPTY_ARRAY = 2;
    static final int NONEMPTY_OBJECT = 5;
    static final int NONEMPTY_DOCUMENT = 6;
    static final int DANGLING_NAME = 4;
  }
}