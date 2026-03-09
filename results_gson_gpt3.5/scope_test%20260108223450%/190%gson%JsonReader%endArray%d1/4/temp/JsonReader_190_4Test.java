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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_190_4Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsPEEKED_END_ARRAY_shouldDecrementStackSizeIncrementPathIndexAndResetPeeked() throws Exception {
    // Set peeked to PEEKED_END_ARRAY (4)
    setField(jsonReader, "peeked", 4);
    // Set stackSize to 2 for safe decrement and index access
    setField(jsonReader, "stackSize", 2);
    // Initialize pathIndices array with length >= 2
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.endArray();

    int stackSize = (int) getField(jsonReader, "stackSize");
    int[] updatedPathIndices = (int[]) getField(jsonReader, "pathIndices");
    int peeked = (int) getField(jsonReader, "peeked");

    assertEquals(1, stackSize, "stackSize should be decremented by 1");
    assertEquals(1, updatedPathIndices[0], "pathIndices[stackSize - 1] should be incremented by 1");
    assertEquals(0, peeked, "peeked should be reset to PEEKED_NONE");
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsPEEKED_NONE_doPeekReturnsPEEKED_END_ARRAY_shouldWorkCorrectly() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    setField(jsonReader, "stackSize", 2);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Mock doPeek() to return PEEKED_END_ARRAY (4)
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(4).when(spyReader).doPeek();

    spyReader.endArray();

    int stackSize = (int) getField(spyReader, "stackSize");
    int[] updatedPathIndices = (int[]) getField(spyReader, "pathIndices");
    int peeked = (int) getField(spyReader, "peeked");

    assertEquals(1, stackSize);
    assertEquals(1, updatedPathIndices[0]);
    assertEquals(0, peeked);
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsNotEndArray_shouldThrowIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 1); // PEEKED_BEGIN_OBJECT (not END_ARRAY)

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonReader.endArray());
    String message = thrown.getMessage();
    assertTrue(message.contains("Expected END_ARRAY but was"));
  }

  // Utility to set private fields
  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Utility to get private fields
  private Object getField(Object target, String fieldName) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}