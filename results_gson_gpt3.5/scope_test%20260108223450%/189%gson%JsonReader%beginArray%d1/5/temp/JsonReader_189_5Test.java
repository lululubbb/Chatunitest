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

public class JsonReader_189_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void beginArray_peekedIsPeekedBeginArray_pushesEmptyArrayAndResetsPeeked() throws Exception {
    setField(jsonReader, "peeked", 3); // PEEKED_BEGIN_ARRAY
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    jsonReader.beginArray();

    int[] pathIndices = getField(jsonReader, "pathIndices");
    int peeked = getField(jsonReader, "peeked");
    int[] stack = getField(jsonReader, "stack");
    int stackSize = getField(jsonReader, "stackSize");

    assertEquals(0, pathIndices[stackSize - 1]);
    assertEquals(0, peeked);
    // Check that stack top is JsonScope.EMPTY_ARRAY (4)
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void beginArray_peekedIsPeekedNone_callsDoPeekAndProcesses() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn(3).when(spyReader).doPeek(); // PEEKED_BEGIN_ARRAY

    spyReader.beginArray();

    int[] pathIndices = getField(spyReader, "pathIndices");
    int peeked = getField(spyReader, "peeked");
    int[] stack = getField(spyReader, "stack");
    int stackSize = getField(spyReader, "stackSize");

    assertEquals(0, pathIndices[stackSize - 1]);
    assertEquals(0, peeked);
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void beginArray_peekedIsNotBeginArray_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (not BEGIN_ARRAY)

    JsonReader spyReader = spy(jsonReader);
    doReturn(5).when(spyReader).doPeek();
    doReturn(JsonToken.BOOLEAN).when(spyReader).peek();
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::beginArray);
    assertTrue(thrown.getMessage().contains("Expected BEGIN_ARRAY but was"));
  }

  // Helpers to set and get private fields using reflection
  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}