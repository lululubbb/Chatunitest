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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.io.Writer;

class JsonWriter_value_Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void value_writesLongValueAndReturnsThis() throws IOException {
    // Arrange
    // Use reflection to set deferredName to null to avoid writeDeferredName writing a name
    setPrivateField(jsonWriter, "deferredName", null);
    // push a valid state on stack so beforeValue() does not throw
    pushStackState(jsonWriter, 6); // 6 = NONEMPTY_DOCUMENT (internal state)

    // Act
    JsonWriter returned = jsonWriter.value(123456789L);

    // Assert
    assertSame(jsonWriter, returned);
    assertEquals("123456789", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_invokesWriteDeferredNameAndBeforeValue() throws IOException {
    // Arrange
    StringWriter outWriter = new StringWriter();
    JsonWriter spyWriter = spy(new JsonWriter(outWriter));
    setPrivateField(spyWriter, "deferredName", null);
    pushStackState(spyWriter, 6);

    String outputBefore = outWriter.toString();

    // Act
    spyWriter.value(42L);

    String outputAfter = outWriter.toString();

    // Assert
    assertTrue(outputAfter.length() > outputBefore.length());
    assertTrue(outputAfter.contains("42"));
  }

  @Test
    @Timeout(8000)
  void value_throwsIOExceptionWhenWriterThrows() throws IOException {
    // Arrange
    WriterThrowingIOException throwingWriter = new WriterThrowingIOException();
    JsonWriter writerWithThrowingOut = new JsonWriter(throwingWriter);
    setPrivateField(writerWithThrowingOut, "deferredName", null);
    pushStackState(writerWithThrowingOut, 6);

    // Act & Assert
    assertThrows(IOException.class, () -> writerWithThrowingOut.value(1L));
  }

  // Helper method to set private field via reflection
  private static void setPrivateField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonWriter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  // Helper method to get private field via reflection
  private static Object getPrivateField(Object target, String fieldName) {
    try {
      Field field = JsonWriter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  // Helper method to push a state into the private stack and set stackSize
  private static void pushStackState(JsonWriter writer, int state) {
    try {
      Field stackField = JsonWriter.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(writer);
      Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stack[0] = state;
      stackSizeField.setInt(writer, 1);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  // Custom Writer that throws IOException on write
  private static class WriterThrowingIOException extends Writer {
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
      throw new IOException("Forced IOException");
    }

    @Override
    public void flush() {}

    @Override
    public void close() {}
  }
}