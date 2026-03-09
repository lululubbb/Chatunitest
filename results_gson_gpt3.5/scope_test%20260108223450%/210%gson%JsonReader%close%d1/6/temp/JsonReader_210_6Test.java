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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class JsonReader_210_6Test {
  private Reader mockReader;
  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and stack to avoid ArrayIndexOutOfBounds in close()
    // Using reflection to set private fields
    try {
      var stackField = JsonReader.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(jsonReader);
      stack[0] = 0; // some initial state
      stackField.set(jsonReader, stack);

      var stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonReader, 1);

      var peekedField = JsonReader.class.getDeclaredField("peeked");
      peekedField.setAccessible(true);
      peekedField.setInt(jsonReader, 999); // some non-PEEKED_NONE value
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void close_shouldResetStateAndCloseReader() throws IOException, Exception {
    // Act
    jsonReader.close();

    // Assert peeked is reset to PEEKED_NONE (which is 0)
    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    int peekedValue = peekedField.getInt(jsonReader);
    assertEquals(0, peekedValue, "peeked should be reset to PEEKED_NONE (0)");

    // Assert stack[0] is set to JsonScope.CLOSED (6)
    var stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonReader);
    assertEquals(JsonScope.CLOSED, stack[0], "stack[0] should be JsonScope.CLOSED");

    // Assert stackSize is set to 1
    var stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonReader);
    assertEquals(1, stackSize, "stackSize should be reset to 1");

    // Verify that Reader.close() was called exactly once
    verify(mockReader, times(1)).close();
  }

  @Test
    @Timeout(8000)
  void close_shouldThrowIOExceptionIfReaderCloseFails() throws IOException {
    doThrow(new IOException("close failed")).when(mockReader).close();

    IOException thrown = assertThrows(IOException.class, () -> jsonReader.close());
    assertEquals("close failed", thrown.getMessage());

    // Even if close throws, peeked and stack should still be reset
    try {
      var peekedField = JsonReader.class.getDeclaredField("peeked");
      peekedField.setAccessible(true);
      int peekedValue = peekedField.getInt(jsonReader);
      assertEquals(0, peekedValue, "peeked should be reset to PEEKED_NONE (0) even if close fails");

      var stackField = JsonReader.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(jsonReader);
      assertEquals(JsonScope.CLOSED, stack[0], "stack[0] should be JsonScope.CLOSED even if close fails");

      var stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      int stackSize = stackSizeField.getInt(jsonReader);
      assertEquals(1, stackSize, "stackSize should be reset to 1 even if close fails");
    } catch (Exception e) {
      fail("Reflection access failed: " + e.getMessage());
    }
  }
}