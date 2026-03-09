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

public class JsonReader_210_3Test {

  private Reader mockReader;
  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void close_shouldResetStateAndCloseReader() throws Exception {
    // Arrange: set non-default values to private fields before close
    setField(jsonReader, "peeked", 123);
    setField(jsonReader, "stack", new int[32]);
    setField(jsonReader, "stackSize", 10);

    // Act
    jsonReader.close();

    // Assert
    assertEquals(0, ((Integer) getField(jsonReader, "peeked")).intValue());
    int[] stack = getField(jsonReader, "stack");
    assertEquals(JsonScope.CLOSED, stack[0]);
    assertEquals(1, ((Integer) getField(jsonReader, "stackSize")).intValue());
    verify(mockReader).close();
  }

  @Test
    @Timeout(8000)
  public void close_shouldPropagateIOException() throws Exception {
    // Arrange
    doThrow(new IOException("close error")).when(mockReader).close();

    // Act & Assert
    try {
      jsonReader.close();
    } catch (IOException e) {
      assertEquals("close error", e.getMessage());
    }
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