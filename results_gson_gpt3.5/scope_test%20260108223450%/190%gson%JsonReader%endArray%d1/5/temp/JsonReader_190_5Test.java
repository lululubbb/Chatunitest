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

public class JsonReader_190_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void endArray_peekedIsPEEKED_END_ARRAY_shouldDecreaseStackSizeAndIncrementPathIndex() throws Exception {
    // Set private fields via reflection
    setField(jsonReader, "peeked", 4); // PEEKED_END_ARRAY
    setField(jsonReader, "stackSize", 2);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.endArray();

    int updatedStackSize = getField(jsonReader, "stackSize");
    int updatedPeeked = getField(jsonReader, "peeked");
    int updatedPathIndex = pathIndices[updatedStackSize - 1];

    assertEquals(1, updatedStackSize);
    assertEquals(0, updatedPeeked);
    assertEquals(1, updatedPathIndex);
  }

  @Test
    @Timeout(8000)
  public void endArray_peekedIsPEEKED_NONE_shouldCallDoPeekAndProceed() throws Exception {
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

    int updatedStackSize = getField(spyReader, "stackSize");
    int updatedPeeked = getField(spyReader, "peeked");
    int updatedPathIndex = pathIndices[updatedStackSize - 1];

    assertEquals(1, updatedStackSize);
    assertEquals(0, updatedPeeked);
    assertEquals(1, updatedPathIndex);
  }

  @Test
    @Timeout(8000)
  public void endArray_peekedIsNotEndArray_shouldThrowIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 1); // PEEKED_BEGIN_OBJECT (not end array)

    Exception exception = assertThrows(IllegalStateException.class, () -> jsonReader.endArray());
    String message = exception.getMessage();

    assertTrue(message.contains("Expected END_ARRAY but was"));
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}