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

class JsonWriterCloseTest {
  private Writer mockWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
    // Initialize stackSize and stack to valid initial state
    try {
      setStackSize(0);
      setStack(new int[32]);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void close_shouldCloseOutAndResetStackSizeWhenCompleteDocument() throws Exception {
    // Arrange: stackSize = 1 and stack[0] = NONEMPTY_DOCUMENT (complete document)
    setStackSize(1);
    setStackAt(0, JsonScope.NONEMPTY_DOCUMENT);

    // Act
    jsonWriter.close();

    // Assert
    verify(mockWriter).close();
    assertEquals(0, getStackSize());
  }

  @Test
    @Timeout(8000)
  void close_shouldThrowIOExceptionWhenStackSizeGreaterThanOne() throws Exception {
    // Arrange: stackSize = 2 (incomplete)
    setStackSize(2);
    setStackAt(0, JsonScope.NONEMPTY_DOCUMENT);
    setStackAt(1, JsonScope.NONEMPTY_ARRAY);

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        jsonWriter.close();
      } finally {
        verify(mockWriter).close();
      }
    });
    assertEquals("Incomplete document", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void close_shouldThrowIOExceptionWhenStackSizeOneButNotNonEmptyDocument() throws Exception {
    // Arrange: stackSize = 1 but stack[0] != NONEMPTY_DOCUMENT
    setStackSize(1);
    setStackAt(0, JsonScope.EMPTY_OBJECT);

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        jsonWriter.close();
      } finally {
        verify(mockWriter).close();
      }
    });
    assertEquals("Incomplete document", thrown.getMessage());
  }

  // Helper methods to set private fields using reflection

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, size);
  }

  private int getStackSize() throws Exception {
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(jsonWriter);
  }

  private void setStackAt(int index, int value) throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[index] = value;
  }

  private void setStack(int[] newStack) throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(jsonWriter, newStack);
  }
}