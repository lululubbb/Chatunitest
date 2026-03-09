package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_210_2Test {

  private Reader mockReader;
  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void close_shouldResetFieldsAndCloseReader() throws Exception {
    // Arrange: set fields to non-default values to verify they are reset
    setField(jsonReader, "peeked", 123);
    setField(jsonReader, "stackSize", 5);
    int[] stack = (int[]) getField(jsonReader, "stack");
    stack[0] = 999;

    // Act
    jsonReader.close();

    // Assert peeked reset to PEEKED_NONE (0)
    assertEquals(0, ((Number) getField(jsonReader, "peeked")).intValue());

    // Assert stack[0] set to JsonScope.CLOSED (6)
    assertEquals(JsonScope.CLOSED, ((int[]) getField(jsonReader, "stack"))[0]);

    // Assert stackSize reset to 1
    assertEquals(1, ((Number) getField(jsonReader, "stackSize")).intValue());

    // Verify reader.close() called
    verify(mockReader).close();
  }

  @Test
    @Timeout(8000)
  void close_shouldThrowIOExceptionWhenReaderCloseFails() throws Exception {
    // Arrange
    doThrow(new IOException("close failed")).when(mockReader).close();

    // Act & Assert
    IOException thrown = null;
    try {
      jsonReader.close();
    } catch (IOException e) {
      thrown = e;
    }
    assertEquals("close failed", thrown.getMessage());
  }

  // Helper to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private fields via reflection
  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    T value = (T) field.get(target);
    return value;
  }
}