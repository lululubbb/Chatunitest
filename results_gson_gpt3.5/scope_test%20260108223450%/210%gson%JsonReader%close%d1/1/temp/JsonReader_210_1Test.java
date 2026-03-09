package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_210_1Test {

  Reader mockReader;
  JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Set stackSize to a value > 0 to ensure stack[0] can be set without ArrayIndexOutOfBounds
    try {
      Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonReader, 1);

      Field stackField = JsonReader.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stackArray = (int[]) stackField.get(jsonReader);
      stackArray[0] = 0; // initial value, will be overwritten in close()
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void close_shouldResetFieldsAndCloseReader() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Arrange: set peeked to a non-zero value to verify it is reset
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, 123);

    // Act
    jsonReader.close();

    // Assert peeked reset to PEEKED_NONE (which is 0)
    assertEquals(0, peekedField.getInt(jsonReader), "peeked should be reset to PEEKED_NONE");

    // Assert stack[0] == JsonScope.CLOSED
    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonReader);
    assertEquals(JsonScope.CLOSED, stack[0], "stack[0] should be set to JsonScope.CLOSED");

    // Assert stackSize == 1
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    assertEquals(1, stackSizeField.getInt(jsonReader), "stackSize should be set to 1");

    // Verify that in.close() was called exactly once
    verify(mockReader, times(1)).close();
  }

  @Test
    @Timeout(8000)
  void close_shouldThrowIOException_whenReaderThrows() throws IOException {
    // Arrange
    doThrow(new IOException("close error")).when(mockReader).close();

    // Act & Assert
    IOException thrown = assertThrows(IOException.class, () -> jsonReader.close());
    assertEquals("close error", thrown.getMessage());

    // Even if exception thrown, peeked and stack should be updated before close() call
    try {
      Field peekedField = JsonReader.class.getDeclaredField("peeked");
      peekedField.setAccessible(true);
      assertEquals(0, peekedField.getInt(jsonReader));

      Field stackField = JsonReader.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(jsonReader);
      assertEquals(JsonScope.CLOSED, stack[0]);

      Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      assertEquals(1, stackSizeField.getInt(jsonReader));
    } catch (Exception e) {
      fail("Reflection assertion failed: " + e.getMessage());
    }
  }
}