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

class JsonReader_190_2Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsPeekedEndArray_decrementsStackSizeAndIncrementsPathIndices() throws Exception {
    // Set peeked to PEEKED_END_ARRAY (4)
    setField(jsonReader, "peeked", 4);
    // Set stackSize to 2 (so stackSize - 1 = 1 is valid index)
    setField(jsonReader, "stackSize", 2);
    // Initialize pathIndices array with length at least 2
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.endArray();

    // After endArray, stackSize should be decremented by 1
    int stackSizeAfter = getField(jsonReader, "stackSize");
    assertEquals(1, stackSizeAfter);

    // pathIndices[stackSizeAfter - 1] should be incremented by 1
    assertEquals(1, pathIndices[0]);

    // peeked should be reset to PEEKED_NONE (0)
    int peekedAfter = getField(jsonReader, "peeked");
    assertEquals(0, peekedAfter);
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsPeekedNone_callsDoPeekAndThrowsIfNotEndArray() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);
    // Mock doPeek() to return something other than PEEKED_END_ARRAY (e.g. PEEKED_BEGIN_OBJECT = 1)
    JsonReader spyReader = spy(jsonReader);
    doReturn(1).when(spyReader).doPeek();

    // Set stackSize and pathIndices to valid values
    setField(spyReader, "stackSize", 2);
    setField(spyReader, "pathIndices", new int[32]);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::endArray);
    assertTrue(thrown.getMessage().startsWith("Expected END_ARRAY but was"));
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsPeekedNone_callsDoPeekAndSucceedsIfEndArray() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);
    // Spy to mock doPeek() returning PEEKED_END_ARRAY (4)
    JsonReader spyReader = spy(jsonReader);
    doReturn(4).when(spyReader).doPeek();

    // Set stackSize and pathIndices to valid values
    setField(spyReader, "stackSize", 2);
    int[] pathIndices = new int[32];
    setField(spyReader, "pathIndices", pathIndices);

    spyReader.endArray();

    int stackSizeAfter = getField(spyReader, "stackSize");
    assertEquals(1, stackSizeAfter);
    assertEquals(1, pathIndices[0]);
    int peekedAfter = getField(spyReader, "peeked");
    assertEquals(0, peekedAfter);
  }

  // Helper method to set private fields via reflection
  private static void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper method to get private int fields via reflection
  private static int getField(Object target, String fieldName) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.getInt(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}