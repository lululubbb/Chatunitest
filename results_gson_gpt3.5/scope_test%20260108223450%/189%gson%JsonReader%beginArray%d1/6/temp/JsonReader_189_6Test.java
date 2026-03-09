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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class JsonReader_189_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void beginArray_peekedIsPeekedBeginArray_pushesScopeAndResetsPeeked() throws Exception {
    // Use reflection to set private int peeked = PEEKED_BEGIN_ARRAY (3)
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, 3);

    // Use reflection to set private int stackSize = 1 (simulate stack with one element)
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);

    // Use reflection to get pathIndices array and set index 0 to some value
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    pathIndices[0] = -1;

    // Call beginArray
    jsonReader.beginArray();

    // Verify push changed stackSize and pathIndices[stackSize - 1] = 0
    int stackSizeAfter = stackSizeField.getInt(jsonReader);
    assertEquals(2, stackSizeAfter);

    int[] pathIndicesAfter = (int[]) pathIndicesField.get(jsonReader);
    assertEquals(0, pathIndicesAfter[stackSizeAfter - 1]);

    // peeked is reset to PEEKED_NONE (0)
    int peekedAfter = peekedField.getInt(jsonReader);
    assertEquals(0, peekedAfter);
  }

  @Test
    @Timeout(8000)
  void beginArray_peekedIsPeekedNone_callsDoPeekAndSucceeds() throws Exception {
    // Set peeked = PEEKED_NONE (0)
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, 0);

    // Spy on jsonReader to mock doPeek()
    JsonReader spyReader = spy(jsonReader);

    // Mock doPeek() to return PEEKED_BEGIN_ARRAY (3)
    doReturn(3).when(spyReader).doPeek();

    // Setup stackSize and pathIndices for push
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
    pathIndices[0] = -1;

    // Call beginArray
    spyReader.beginArray();

    // Verify doPeek was called
    verify(spyReader).doPeek();

    // Verify stackSize increased by 1
    int stackSizeAfter = stackSizeField.getInt(spyReader);
    assertEquals(2, stackSizeAfter);

    // Verify pathIndices updated at new top
    int[] pathIndicesAfter = (int[]) pathIndicesField.get(spyReader);
    assertEquals(0, pathIndicesAfter[stackSizeAfter - 1]);

    // Verify peeked reset to PEEKED_NONE
    Field peekedFieldAfter = JsonReader.class.getDeclaredField("peeked");
    peekedFieldAfter.setAccessible(true);
    int peekedAfter = peekedFieldAfter.getInt(spyReader);
    assertEquals(0, peekedAfter);
  }

  @Test
    @Timeout(8000)
  void beginArray_wrongPeeked_throwsIllegalStateException() throws Exception {
    // Set peeked to a token other than PEEKED_BEGIN_ARRAY or PEEKED_NONE, e.g. PEEKED_BEGIN_OBJECT (1)
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, 1);

    // Spy on jsonReader to mock peek() and locationString()
    JsonReader spyReader = spy(jsonReader);

    // Mock peek() to return a JsonToken (simulate JsonToken.BEGIN_OBJECT)
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    // Mock locationString() to return ": at path $"
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    doReturn(": at path $").when(spyReader).locationString();

    IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::beginArray);
    assertTrue(exception.getMessage().contains("Expected BEGIN_ARRAY but was BEGIN_OBJECT"));
  }
}