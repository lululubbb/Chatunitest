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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterFlushTest {
  private Writer outMock;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() throws Exception {
    outMock = mock(Writer.class);
    jsonWriter = new JsonWriter(outMock);
    // Use reflection to set stackSize to 1 to simulate open JsonWriter (not closed)
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);
  }

  @Test
    @Timeout(8000)
  public void flush_whenStackSizeZero_throwsIllegalStateException() throws Exception {
    // Set stackSize to 0 to simulate closed JsonWriter
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 0);

    // Directly call flush() instead of reflection to avoid InvocationTargetException wrapping
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonWriter.flush());
    assertEquals("JsonWriter is closed.", ex.getMessage());
    verifyNoInteractions(outMock);
  }

  @Test
    @Timeout(8000)
  public void flush_whenStackSizeNonZero_callsOutFlush() throws Exception {
    // Directly call flush() instead of reflection
    jsonWriter.flush();

    verify(outMock, times(1)).flush();
  }

  @Test
    @Timeout(8000)
  public void flush_outFlushThrowsIOException_propagatesIOException() throws Exception {
    doThrow(new IOException("flush failed")).when(outMock).flush();

    IOException ex = assertThrows(IOException.class, () -> jsonWriter.flush());
    assertEquals("flush failed", ex.getMessage());
  }
}