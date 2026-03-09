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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_398_3Test {
  private Writer mockWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void close_closesWriterSuccessfullyAndResetsStackSize() throws Exception {
    // Arrange: set stackSize = 1 and stack[0] = NONEMPTY_DOCUMENT (valid state)
    setStackSize(1);
    setStackValue(0, JsonScope.NONEMPTY_DOCUMENT);

    // Act
    jsonWriter.close();

    // Assert
    verify(mockWriter).close();
    // stackSize should be reset to 0 after close
    int stackSizeAfter = getStackSize();
    org.junit.jupiter.api.Assertions.assertEquals(0, stackSizeAfter);
  }

  @Test
    @Timeout(8000)
  public void close_throwsIOExceptionIfStackSizeGreaterThan1() throws Exception {
    // Arrange: set stackSize = 2 (invalid state)
    setStackSize(2);
    setStackValue(0, JsonScope.NONEMPTY_DOCUMENT);
    setStackValue(1, JsonScope.NONEMPTY_OBJECT);

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, () -> jsonWriter.close());
    org.junit.jupiter.api.Assertions.assertEquals("Incomplete document", thrown.getMessage());
    verify(mockWriter).close();
  }

  @Test
    @Timeout(8000)
  public void close_throwsIOExceptionIfStackSizeIs1ButTopIsNotNonemptyDocument() throws Exception {
    // Arrange: set stackSize = 1 but stack[0] != NONEMPTY_DOCUMENT (invalid state)
    setStackSize(1);
    setStackValue(0, JsonScope.EMPTY_OBJECT);

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, () -> jsonWriter.close());
    org.junit.jupiter.api.Assertions.assertEquals("Incomplete document", thrown.getMessage());
    verify(mockWriter).close();
  }

  @Test
    @Timeout(8000)
  public void close_propagatesIOExceptionFromWriterClose() throws Exception {
    // Arrange: simulate IOException when closing underlying writer
    doThrow(new IOException("Writer close failed")).when(mockWriter).close();

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, () -> jsonWriter.close());
    org.junit.jupiter.api.Assertions.assertEquals("Writer close failed", thrown.getMessage());
  }

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

  private void setStackValue(int index, int value) throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stackArray = (int[]) stackField.get(jsonWriter);
    stackArray[index] = value;
  }
}