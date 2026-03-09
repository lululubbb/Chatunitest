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

public class JsonReader_202_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsPeekedNull_resetsPeekedAndIncrementsPathIndices() throws Exception {
    // Use reflection to set private fields
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    // Setup initial state
    peekedField.setInt(jsonReader, 7); // PEEKED_NULL = 7
    stackSizeField.setInt(jsonReader, 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 5;
    pathIndicesField.set(jsonReader, pathIndices);

    jsonReader.nextNull();

    // Assert peeked reset to PEEKED_NONE (0)
    assertEquals(0, peekedField.getInt(jsonReader));
    // Assert pathIndices[stackSize-1] incremented by 1
    assertEquals(6, ((int[]) pathIndicesField.get(jsonReader))[0]);
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsPeekedNone_callsDoPeekAndProcessesPeekedNull() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    // Set peeked to PEEKED_NONE to force doPeek call
    peekedField.setInt(jsonReader, 0);
    stackSizeField.setInt(jsonReader, 1);
    int[] pathIndices = new int[32];
    pathIndices[0] = 3;
    pathIndicesField.set(jsonReader, pathIndices);

    // Spy on jsonReader to mock doPeek
    JsonReader spyReader = spy(jsonReader);
    doReturn(7).when(spyReader).doPeek();

    spyReader.nextNull();

    // peeked reset to PEEKED_NONE
    assertEquals(0, peekedField.getInt(spyReader));
    // pathIndices incremented
    assertEquals(4, ((int[]) pathIndicesField.get(spyReader))[0]);
    // Verify doPeek called once
    verify(spyReader, times(1)).doPeek();
  }

  @Test
    @Timeout(8000)
  public void nextNull_peekedIsNotNull_throwsIllegalStateException() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);

    // Set peeked to a value other than PEEKED_NULL or PEEKED_NONE
    peekedField.setInt(jsonReader, 1); // PEEKED_BEGIN_OBJECT

    // Spy on jsonReader to mock peek() and locationString()
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    doReturn(" at line 1 column 1 path $").when(spyReader).locationString();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextNull);
    assertTrue(thrown.getMessage().contains("Expected null but was BEGIN_OBJECT at line 1 column 1 path $"));
  }
}