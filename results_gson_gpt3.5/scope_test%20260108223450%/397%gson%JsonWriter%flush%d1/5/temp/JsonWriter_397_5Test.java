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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterFlushTest {
  private Writer mockWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() throws Exception {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);

    // Initialize stackSize and stack properly by calling beginArray() to simulate open state
    jsonWriter.beginArray();
  }

  @Test
    @Timeout(8000)
  void flush_whenStackSizeZero_throwsIllegalStateException() throws Exception {
    // Use reflection to set stackSize to 0 (simulate closed writer)
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 0);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonWriter.flush());
    assertEquals("JsonWriter is closed.", ex.getMessage());

    // Verify out.flush() is never called
    verify(mockWriter, never()).flush();
  }

  @Test
    @Timeout(8000)
  void flush_whenStackSizeGreaterThanZero_callsOutFlush() throws Exception {
    // stackSize is already > 0 due to beginArray() in setUp()

    // Call flush
    jsonWriter.flush();

    // Verify out.flush() called once
    verify(mockWriter, times(1)).flush();
  }

  @Test
    @Timeout(8000)
  void flush_whenUnderlyingWriterThrowsIOException_propagatesIOException() throws Exception {
    // stackSize is already > 0 due to beginArray() in setUp()

    doThrow(new IOException("flush failed")).when(mockWriter).flush();

    IOException ex = assertThrows(IOException.class, () -> jsonWriter.flush());
    assertEquals("flush failed", ex.getMessage());
  }
}