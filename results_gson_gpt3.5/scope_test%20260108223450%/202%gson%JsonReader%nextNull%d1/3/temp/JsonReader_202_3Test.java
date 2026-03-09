package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_202_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsPeekedNull_shouldConsumeNull() throws Exception {
    // Set peeked field to PEEKED_NULL (7)
    setField(jsonReader, "peeked", 7);

    // Set stackSize to 1 and pathIndices array with initial value 0
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    // Call nextNull and verify peeked reset and pathIndices incremented
    jsonReader.nextNull();

    int peekedAfter = getField(jsonReader, "peeked");
    assertEquals(0, peekedAfter, "peeked should be reset to PEEKED_NONE (0)");

    int[] pathIndicesAfter = getField(jsonReader, "pathIndices");
    assertEquals(1, pathIndicesAfter[0], "pathIndices[0] should be incremented by 1");
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsPeekedNone_doPeekReturnsPeekedNull_shouldConsumeNull() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);

    // Spy on jsonReader to mock doPeek() and peek()
    JsonReader spyReader = spy(jsonReader);

    // Mock doPeek to return PEEKED_NULL (7)
    doReturn(7).when(spyReader).doPeek();

    // Set stackSize and pathIndices
    setField(spyReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(spyReader, "pathIndices", pathIndices);

    // Call nextNull
    spyReader.nextNull();

    // Verify peeked reset and pathIndices incremented
    int peekedAfter = getField(spyReader, "peeked");
    assertEquals(0, peekedAfter);

    int[] pathIndicesAfter = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndicesAfter[0]);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsNotNull_shouldThrowIllegalStateException() throws Exception {
    // Set peeked to a value not null and not none, e.g. PEEKED_TRUE (5)
    setField(jsonReader, "peeked", 5);

    // Spy on jsonReader to mock peek() and locationString()
    JsonReader spyReader = spy(jsonReader);

    // Mock peek() to return JsonReader.JsonToken.TRUE (use fully qualified enum constant)
    doReturn(JsonReader.JsonToken.TRUE).when(spyReader).peek();

    // Mock locationString() to return ": at path $"
    doReturn(": at path $").when(spyReader).locationString();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextNull);
    assertTrue(thrown.getMessage().contains("Expected null but was TRUE: at path $"));
  }

  // Helper method to set private fields via reflection
  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }

  private void setField(Object instance, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(instance, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}