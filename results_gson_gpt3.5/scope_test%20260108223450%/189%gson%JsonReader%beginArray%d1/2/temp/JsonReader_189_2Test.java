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

public class JsonReader_189_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void beginArray_peekedIsPeekedBeginArray_shouldPushEmptyArrayAndResetPeeked() throws Exception {
    // Set peeked to PEEKED_BEGIN_ARRAY (3)
    setField(jsonReader, "peeked", 3);
    // Set stackSize to 1 (simulate one element in stack)
    setField(jsonReader, "stackSize", 1);
    // Initialize stack and pathIndices arrays properly
    setField(jsonReader, "stack", new int[32]);
    setField(jsonReader, "pathIndices", new int[32]);

    jsonReader.beginArray();

    int[] stack = getField(jsonReader, "stack");
    int stackSize = getField(jsonReader, "stackSize");
    int[] pathIndices = getField(jsonReader, "pathIndices");
    int peeked = getField(jsonReader, "peeked");

    // After beginArray, stackSize should have increased by 1
    assertEquals(2, stackSize);
    // The top of the stack should be JsonScope.EMPTY_ARRAY (which is 1)
    // JsonScope.EMPTY_ARRAY is a package-private enum int constant 1 in Gson source
    assertEquals(1, stack[stackSize - 1]);
    // The pathIndices for the new top should be set to 0
    assertEquals(0, pathIndices[stackSize - 1]);
    // peeked should be reset to PEEKED_NONE (0)
    assertEquals(0, peeked);
  }

  @Test
    @Timeout(8000)
  public void beginArray_peekedIsPeekedNone_callsDoPeekAndSucceeds() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);
    // Spy on jsonReader to mock doPeek and peek methods
    JsonReader spyReader = spy(jsonReader);

    // Mock doPeek to return PEEKED_BEGIN_ARRAY (3)
    doReturn(3).when(spyReader).doPeek();
    // Set stackSize to 1 for push
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "stack", new int[32]);
    setField(spyReader, "pathIndices", new int[32]);

    spyReader.beginArray();

    int[] stack = getField(spyReader, "stack");
    int stackSize = getField(spyReader, "stackSize");
    int[] pathIndices = getField(spyReader, "pathIndices");
    int peeked = getField(spyReader, "peeked");

    assertEquals(2, stackSize);
    assertEquals(1, stack[stackSize - 1]);
    assertEquals(0, pathIndices[stackSize - 1]);
    assertEquals(0, peeked);
  }

  @Test
    @Timeout(8000)
  public void beginArray_wrongPeeked_throwsIllegalStateException() throws Exception {
    // Set peeked to an invalid token for beginArray (e.g. PEEKED_BEGIN_OBJECT = 1)
    setField(jsonReader, "peeked", 1);
    // Spy on jsonReader to mock peek and locationString methods
    JsonReader spyReader = spy(jsonReader);

    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::beginArray);
    assertTrue(exception.getMessage().contains("Expected BEGIN_ARRAY but was"));
    assertTrue(exception.getMessage().contains("BEGIN_OBJECT"));
    assertTrue(exception.getMessage().contains("at path $"));
  }

  // Utility methods for reflection access

  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }

  private void setField(Object instance, String fieldName, Object value) {
    try {
      Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(instance, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}