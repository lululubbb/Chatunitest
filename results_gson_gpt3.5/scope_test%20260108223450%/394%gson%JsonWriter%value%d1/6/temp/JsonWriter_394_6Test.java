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

import com.google.gson.stream.JsonWriter;

class JsonWriter_value_Test {

  private Writer mockWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  @Test
    @Timeout(8000)
  void value_writesLongValue_and_returnsThis() throws IOException {
    // Arrange
    setPrivateField(jsonWriter, "deferredName", null);
    pushStackState(jsonWriter, 6); // NONEMPTY_DOCUMENT = 6

    // Act
    JsonWriter returned = jsonWriter.value(123456789L);

    // Assert
    assertSame(jsonWriter, returned);
    verify(mockWriter).write("123456789");
  }

  @Test
    @Timeout(8000)
  void value_withDeferredName_writesNameAndValue() throws Exception {
    // Arrange
    setPrivateField(jsonWriter, "deferredName", "keyName");
    pushStackState(jsonWriter, 1); // EMPTY_OBJECT = 1

    // Act
    JsonWriter returned = jsonWriter.value(42L);

    // Assert
    assertSame(jsonWriter, returned);
    assertNull(getPrivateField(jsonWriter, "deferredName"));
    verify(mockWriter).write("\"keyName\"");
    verify(mockWriter).write("42");
  }

  @Test
    @Timeout(8000)
  void value_throwsIOException_whenWriterThrows() throws IOException {
    // Arrange
    setPrivateField(jsonWriter, "deferredName", null);
    pushStackState(jsonWriter, 6);
    doThrow(new IOException("write error")).when(mockWriter).write(anyString());

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, () -> jsonWriter.value(1L));
    assertEquals("write error", thrown.getMessage());
  }

  // Helper to set private field via reflection
  private static void setPrivateField(Object target, String fieldName, Object value) {
    try {
      Field field = getField(target.getClass(), fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get private field via reflection
  private static Object getPrivateField(Object target, String fieldName) {
    try {
      Field field = getField(target.getClass(), fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to push a valid state onto the stack array and increment stackSize
  private static void pushStackState(JsonWriter writer, int state) {
    try {
      Field stackField = getField(writer.getClass(), "stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(writer);
      Field stackSizeField = getField(writer.getClass(), "stackSize");
      stackSizeField.setAccessible(true);
      int stackSize = (int) stackSizeField.get(writer);
      stack[stackSize] = state;
      stackSizeField.set(writer, stackSize + 1);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get Field, including from superclasses
  private static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(name);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(name);
  }
}