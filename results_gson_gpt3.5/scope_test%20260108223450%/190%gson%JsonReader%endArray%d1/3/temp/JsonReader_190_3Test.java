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

public class JsonReader_190_3Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() throws Exception {
    Reader reader = mock(Reader.class);
    jsonReader = new JsonReader(reader);

    // Initialize stackSize and pathIndices for tests
    setField(jsonReader, "stackSize", 2);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 0;
    setField(jsonReader, "pathIndices", pathIndices);
  }

  @Test
    @Timeout(8000)
  public void endArray_peekedIsPeekedEndArray_decrementsStackSizeAndIncrementsPathIndex() throws Exception {
    // Set peeked to PEEKED_END_ARRAY (4)
    setField(jsonReader, "peeked", 4);

    int initialStackSize = getField(jsonReader, "stackSize");
    int[] pathIndices = getField(jsonReader, "pathIndices");
    int initialPathIndex = pathIndices[initialStackSize - 1];

    jsonReader.endArray();

    int newStackSize = getField(jsonReader, "stackSize");
    int[] newPathIndices = getField(jsonReader, "pathIndices");
    int newPathIndex = newPathIndices[newStackSize - 1];
    int peeked = getField(jsonReader, "peeked");

    assertEquals(initialStackSize - 1, newStackSize, "stackSize should be decremented by 1");
    assertEquals(initialPathIndex + 1, newPathIndex, "pathIndices at stackSize-1 should be incremented by 1");
    assertEquals(0, peeked, "peeked should be reset to PEEKED_NONE");
  }

  @Test
    @Timeout(8000)
  public void endArray_peekedIsPeekedNone_callsDoPeekAndEndsArraySuccessfully() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);

    // Spy on jsonReader to mock doPeek
    JsonReader spyReader = spy(jsonReader);

    // Mock doPeek to return PEEKED_END_ARRAY (4)
    doReturn(4).when(spyReader).doPeek();

    int initialStackSize = getField(spyReader, "stackSize");
    int[] pathIndices = getField(spyReader, "pathIndices");
    int initialPathIndex = pathIndices[initialStackSize - 1];

    spyReader.endArray();

    int newStackSize = getField(spyReader, "stackSize");
    int[] newPathIndices = getField(spyReader, "pathIndices");
    int newPathIndex = newPathIndices[newStackSize - 1];
    int peeked = getField(spyReader, "peeked");

    assertEquals(initialStackSize - 1, newStackSize, "stackSize should be decremented by 1");
    assertEquals(initialPathIndex + 1, newPathIndex, "pathIndices at stackSize-1 should be incremented by 1");
    assertEquals(0, peeked, "peeked should be reset to PEEKED_NONE");
  }

  @Test
    @Timeout(8000)
  public void endArray_peekedIsNotEndArray_throwsIllegalStateException() throws Exception {
    // Set peeked to a value not equal to PEEKED_END_ARRAY or PEEKED_NONE (e.g. PEEKED_BEGIN_ARRAY=3)
    setField(jsonReader, "peeked", 3);

    // Spy on jsonReader to mock peek() and locationString()
    JsonReader spyReader = spy(jsonReader);

    doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();
    doReturn(" at line 1 column 1").when(spyReader).locationString();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::endArray);
    assertTrue(thrown.getMessage().contains("Expected END_ARRAY but was BEGIN_ARRAY at line 1 column 1"));
  }

  // Utility methods to get and set private fields via reflection
  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }

  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }
}