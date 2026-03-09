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

class JsonReader_210_4Test {

  JsonReader jsonReader;
  Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // set internal fields to non-default values to verify reset after close
    setField(jsonReader, "peeked", 5);
    setField(jsonReader, "stack", new int[] {1, 2, 3, 4});
    setField(jsonReader, "stackSize", 4);
  }

  @Test
    @Timeout(8000)
  void close_resetsFieldsAndClosesReader() throws IOException {
    jsonReader.close();

    int peeked = (int) getField(jsonReader, "peeked");
    int[] stack = (int[]) getField(jsonReader, "stack");
    int stackSize = (int) getField(jsonReader, "stackSize");

    assertEquals(0, peeked, "peeked should be reset to PEEKED_NONE (0)");
    assertEquals(JsonScope.CLOSED, stack[0], "stack[0] should be set to JsonScope.CLOSED");
    assertEquals(1, stackSize, "stackSize should be reset to 1");

    verify(mockReader).close();
  }

  @Test
    @Timeout(8000)
  void close_whenReaderThrowsIOException_throwsIOException() throws IOException {
    doThrow(new IOException("close failed")).when(mockReader).close();

    IOException thrown = assertThrows(IOException.class, () -> jsonReader.close());
    assertEquals("close failed", thrown.getMessage());

    // Even if close throws, peeked, stack and stackSize should be reset
    int peeked = (int) getField(jsonReader, "peeked");
    int[] stack = (int[]) getField(jsonReader, "stack");
    int stackSize = (int) getField(jsonReader, "stackSize");

    assertEquals(0, peeked);
    assertEquals(JsonScope.CLOSED, stack[0]);
    assertEquals(1, stackSize);
  }

  private static Object getField(Object target, String fieldName) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      var field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}