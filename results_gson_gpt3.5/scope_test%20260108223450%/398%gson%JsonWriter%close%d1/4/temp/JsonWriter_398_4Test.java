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

public class JsonWriter_398_4Test {
  private JsonWriter jsonWriter;
  private Writer mockWriter;

  @BeforeEach
  public void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void close_closesWriterAndResetsStackSize_whenStackSizeIsZero() throws IOException, NoSuchFieldException, IllegalAccessException {
    // stackSize = 0, so no exception expected
    setStackSize(0);

    jsonWriter.close();

    verify(mockWriter).close();
    assertEquals(0, getStackSize());
  }

  @Test
    @Timeout(8000)
  public void close_closesWriterAndThrowsIOException_whenStackSizeGreaterThanOne() throws IOException, NoSuchFieldException, IllegalAccessException {
    setStackSize(2);
    setStackTop(JsonScope.EMPTY_DOCUMENT); // stack top value does not matter here

    IOException thrown = assertThrows(IOException.class, () -> jsonWriter.close());

    verify(mockWriter).close();
    assertEquals("Incomplete document", thrown.getMessage());
    // stackSize should be reset to 0 after close
    assertEquals(0, getStackSize());
  }

  @Test
    @Timeout(8000)
  public void close_closesWriterAndThrowsIOException_whenStackSizeIsOneAndTopIsNotNonEmptyDocument() throws IOException, NoSuchFieldException, IllegalAccessException {
    setStackSize(1);
    setStackTop(JsonScope.EMPTY_OBJECT); // any value != NONEMPTY_DOCUMENT

    IOException thrown = assertThrows(IOException.class, () -> jsonWriter.close());

    verify(mockWriter).close();
    assertEquals("Incomplete document", thrown.getMessage());
    // stackSize should be reset to 0 after close
    assertEquals(0, getStackSize());
  }

  @Test
    @Timeout(8000)
  public void close_closesWriterAndDoesNotThrow_whenStackSizeIsOneAndTopIsNonEmptyDocument() throws IOException, NoSuchFieldException, IllegalAccessException {
    setStackSize(1);
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);

    jsonWriter.close();

    verify(mockWriter).close();
    assertEquals(0, getStackSize());
  }

  private void setStackSize(int size) throws NoSuchFieldException, IllegalAccessException {
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, size);
  }

  private int getStackSize() throws NoSuchFieldException, IllegalAccessException {
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(jsonWriter);
  }

  private void setStackTop(int value) throws NoSuchFieldException, IllegalAccessException {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int size = stackSizeField.getInt(jsonWriter);

    if (size == 0) {
      // Initialize stack and stackSize properly
      stack[0] = value;
      stackSizeField.setInt(jsonWriter, 1);
    } else {
      stack[size - 1] = value;
    }
  }
}