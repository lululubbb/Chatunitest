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

public class JsonReader_189_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void beginArray_peekedIsPeekedBeginArray_pushCalledAndStateUpdated() throws Exception {
    // Set peeked to PEEKED_BEGIN_ARRAY (3)
    setField(jsonReader, "peeked", 3);
    // Set stackSize to 1 (simulate stack has one element)
    setField(jsonReader, "stackSize", 1);
    // Initialize pathIndices array with length >= stackSize
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Call beginArray()
    jsonReader.beginArray();

    // Verify peeked is reset to PEEKED_NONE (0)
    int peeked = (int) getField(jsonReader, "peeked");
    assertEquals(0, peeked);

    // Verify pathIndices[stackSize - 1] == 0
    assertEquals(0, pathIndices[0]);

    // Verify stack top is JsonScope.EMPTY_ARRAY (4)
    int[] stack = (int[]) getField(jsonReader, "stack");
    int stackSize = (int) getField(jsonReader, "stackSize");
    assertEquals(2, stackSize); // stackSize incremented by push
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void beginArray_peekedIsPeekedNone_callsDoPeekAndSucceeds() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);
    // Set stackSize to 1
    setField(jsonReader, "stackSize", 1);
    // Initialize pathIndices array
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Spy on jsonReader to stub doPeek()
    JsonReader spyReader = spy(jsonReader);
    doReturn(3).when(spyReader).doPeek();

    // Call beginArray()
    spyReader.beginArray();

    // Verify peeked reset
    int peeked = (int) getField(spyReader, "peeked");
    assertEquals(0, peeked);

    // Verify pathIndices[stackSize - 1] == 0
    assertEquals(0, pathIndices[0]);

    // Verify stack top is JsonScope.EMPTY_ARRAY (4)
    int[] stack = (int[]) getField(spyReader, "stack");
    int stackSize = (int) getField(spyReader, "stackSize");
    assertEquals(2, stackSize);
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void beginArray_peekedIsInvalid_throwsIllegalStateException() throws Exception {
    // Set peeked to an invalid value (e.g., PEEKED_BEGIN_OBJECT = 1)
    setField(jsonReader, "peeked", 1);

    // Stub peek() to return some JsonToken (simulate wrong token)
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    // Stub locationString() to return a known string
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::beginArray);
    assertTrue(ex.getMessage().contains("Expected BEGIN_ARRAY but was BEGIN_OBJECT at path $"));
  }

  // Helper methods to access private fields and methods
  private static Object getField(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}