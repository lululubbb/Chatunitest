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

class JsonReader_202_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void nextNull_whenPeekedIsPeekedNull_shouldConsumeNullAndIncrementPathIndex() throws Exception {
    // Set peeked to PEEKED_NULL (7)
    setField(jsonReader, "peeked", Integer.valueOf(7));
    setField(jsonReader, "stackSize", Integer.valueOf(1));
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.nextNull();

    assertEquals(Integer.valueOf(0), getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextNull_whenPeekedIsPeekedNoneAndDoPeekReturnsPeekedNull_shouldConsumeNullAndIncrementPathIndex() throws Exception {
    setField(jsonReader, "peeked", Integer.valueOf(0));
    setField(jsonReader, "stackSize", Integer.valueOf(1));
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Spy on jsonReader to mock doPeek()
    JsonReader spyReader = spy(jsonReader);
    doReturn(7).when(spyReader).doPeek();

    spyReader.nextNull();

    assertEquals(Integer.valueOf(0), getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextNull_whenPeekedIsNotNull_shouldThrowIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", Integer.valueOf(5));
    setField(jsonReader, "stackSize", Integer.valueOf(1));
    // We need to mock peek() and locationString() to avoid null pointer and get proper message
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BOOLEAN).when(spyReader).peek();
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::nextNull);
    assertTrue(ex.getMessage().contains("Expected null but was BOOLEAN at path $"));
  }

  // Helper methods to access private fields via reflection
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    T value = (T) field.get(target);
    return value;
  }
}