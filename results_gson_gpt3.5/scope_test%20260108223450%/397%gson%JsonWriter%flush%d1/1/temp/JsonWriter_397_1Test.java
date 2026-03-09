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

class JsonWriter_397_1Test {

  private Writer mockWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  void flush_withOpenWriter_flushesOut() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Arrange: stackSize must be > 0 to avoid IllegalStateException
    setStackSize(jsonWriter, 1);

    // Act
    jsonWriter.flush();

    // Assert
    verify(mockWriter).flush();
  }

  @Test
    @Timeout(8000)
  void flush_withClosedWriter_throwsIllegalStateException() throws NoSuchFieldException, IllegalAccessException {
    // Arrange: stackSize = 0 means closed
    setStackSize(jsonWriter, 0);

    // Act & Assert
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> jsonWriter.flush());
    assertEquals("JsonWriter is closed.", exception.getMessage());
  }

  private void setStackSize(JsonWriter writer, int size) throws NoSuchFieldException, IllegalAccessException {
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, size);
  }
}