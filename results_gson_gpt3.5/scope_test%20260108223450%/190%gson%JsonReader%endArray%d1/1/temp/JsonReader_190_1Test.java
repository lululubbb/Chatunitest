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

class JsonReader_190_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsPEEKED_END_ARRAY_decrementsStackSizeAndIncrementsPathIndex() throws Exception {
    // Set peeked to PEEKED_END_ARRAY (4)
    setField(jsonReader, "peeked", 4);
    // Set stackSize to 2 to allow decrement
    setField(jsonReader, "stackSize", 2);
    // Initialize pathIndices array with size at least 2 and set value at 1 to 0
    int[] pathIndices = new int[32];
    pathIndices[1] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.endArray();

    // Assert stackSize decremented by 1
    int updatedStackSize = getField(jsonReader, "stackSize");
    assertEquals(1, updatedStackSize);

    // Assert pathIndices[stackSize - 1] incremented by 1
    int[] updatedPathIndices = getField(jsonReader, "pathIndices");
    assertEquals(1, updatedPathIndices[0]);

    // Assert peeked reset to PEEKED_NONE (0)
    int peeked = getField(jsonReader, "peeked");
    assertEquals(0, peeked);
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsPEEKED_NONE_callsDoPeekAndBehavesCorrectly() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);

    // Mock doPeek() to return PEEKED_END_ARRAY (4)
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(4).when(spyReader).doPeek();

    // Setup stackSize and pathIndices as in first test
    setField(spyReader, "stackSize", 2);
    int[] pathIndices = new int[32];
    pathIndices[1] = 0;
    setField(spyReader, "pathIndices", pathIndices);

    spyReader.endArray();

    // Assert stackSize decremented by 1
    int updatedStackSize = getField(spyReader, "stackSize");
    assertEquals(1, updatedStackSize);

    // Assert pathIndices[stackSize - 1] incremented by 1
    int[] updatedPathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, updatedPathIndices[0]);

    // Assert peeked reset to PEEKED_NONE (0)
    int peeked = getField(spyReader, "peeked");
    assertEquals(0, peeked);
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsNotEndArray_throwsIllegalStateException() throws Exception {
    // Set peeked to a value not equal to PEEKED_END_ARRAY or PEEKED_NONE, e.g. PEEKED_BEGIN_ARRAY (3)
    setField(jsonReader, "peeked", 3);

    // Mock peek() to return a JsonToken (use BEGIN_ARRAY)
    JsonToken mockToken = JsonToken.BEGIN_ARRAY;
    JsonReader spyReader = spy(jsonReader);
    doReturn(mockToken).when(spyReader).peek();

    // Mock locationString() to return ":location"
    doReturn(":location").when(spyReader).locationString();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::endArray);
    assertTrue(thrown.getMessage().contains("Expected END_ARRAY but was " + mockToken + ":location"));
  }

  // Helper method to set private field value via reflection
  private <T> void setField(Object target, String fieldName, T value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper method to get private field value via reflection
  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}