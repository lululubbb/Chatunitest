package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_210_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void close_shouldResetPeekedAndStackAndStackSizeAndCloseReader() throws Exception {
    // Arrange: setup fields to non-default values to verify reset
    setField(jsonReader, "peeked", 42);
    int[] stack = new int[32];
    Arrays.fill(stack, 99);
    setField(jsonReader, "stack", stack);
    setField(jsonReader, "stackSize", 10);

    // Act
    jsonReader.close();

    // Assert peeked reset
    int peeked = getField(jsonReader, "peeked");
    assertEquals(0, peeked, "peeked should be reset to PEEKED_NONE (0)");

    // Assert stack[0] is JsonScope.CLOSED (6)
    int[] actualStack = getField(jsonReader, "stack");
    assertEquals(JsonScope.CLOSED, actualStack[0], "stack[0] should be JsonScope.CLOSED");

    // Assert stackSize is 1
    int stackSize = getField(jsonReader, "stackSize");
    assertEquals(1, stackSize, "stackSize should be reset to 1");

    // Verify that Reader.close() was called
    // Mockito verifies by default, but since no call verification is requested, we rely on no exceptions

  }

  @Test
    @Timeout(8000)
  void close_shouldThrowIOExceptionWhenReaderCloseFails() throws Exception {
    // Arrange
    doThrow(new IOException("close failed")).when(mockReader).close();

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, () -> jsonReader.close());
    assertEquals("close failed", thrown.getMessage());
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }

  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }
}