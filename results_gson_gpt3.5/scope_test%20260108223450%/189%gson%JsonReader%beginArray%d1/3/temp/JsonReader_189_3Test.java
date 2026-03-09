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

public class JsonReader_189_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void beginArray_peekedIsPeekedBeginArray_shouldPushAndResetPeeked() throws Exception {
    // Set peeked to PEEKED_BEGIN_ARRAY (3)
    setField(jsonReader, "peeked", 3);
    // Set stackSize to 1 to avoid ArrayIndexOutOfBounds when accessing pathIndices
    setField(jsonReader, "stackSize", 1);
    // Initialize pathIndices array
    setField(jsonReader, "pathIndices", new int[32]);
    // Initialize stack array with size 32
    setField(jsonReader, "stack", new int[32]);

    jsonReader.beginArray();

    // Verify peeked is reset to PEEKED_NONE (0)
    int peeked = getField(jsonReader, "peeked");
    assertEquals(0, peeked);

    // Verify pathIndices[stackSize-1] is set to 0
    int[] pathIndices = getField(jsonReader, "pathIndices");
    int stackSize = getField(jsonReader, "stackSize");
    assertEquals(0, pathIndices[stackSize - 1]);

    // Verify stack top is JsonScope.EMPTY_ARRAY (4)
    int[] stack = getField(jsonReader, "stack");
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void beginArray_peekedIsNone_callsDoPeekAndBehavesCorrectly() throws Exception {
    // peeked = PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);
    // stackSize = 1
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "stack", new int[32]);

    // Spy on jsonReader to mock doPeek and peek methods
    JsonReader spyReader = spy(jsonReader);

    // doPeek returns PEEKED_BEGIN_ARRAY (3)
    doReturn(3).when(spyReader).doPeek();

    spyReader.beginArray();

    // peeked reset to PEEKED_NONE (0)
    int peeked = getField(spyReader, "peeked");
    assertEquals(0, peeked);

    int[] pathIndices = getField(spyReader, "pathIndices");
    int stackSize = getField(spyReader, "stackSize");
    assertEquals(0, pathIndices[stackSize - 1]);

    int[] stack = getField(spyReader, "stack");
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);

    verify(spyReader).doPeek();
  }

  @Test
    @Timeout(8000)
  void beginArray_peekedIsInvalid_throwsIllegalStateException() throws Exception {
    // peeked = PEEKED_BEGIN_OBJECT (1) which is invalid for beginArray
    setField(jsonReader, "peeked", 1);

    // Spy to mock peek() and locationString()
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::beginArray);
    assertTrue(ex.getMessage().contains("Expected BEGIN_ARRAY but was"));
    assertTrue(ex.getMessage().contains("BEGIN_OBJECT"));
    assertTrue(ex.getMessage().contains(" at path $"));
  }

  // Reflection helper methods
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