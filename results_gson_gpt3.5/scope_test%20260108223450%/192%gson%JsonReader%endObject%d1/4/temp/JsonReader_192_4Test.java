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

public class JsonReader_192_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void endObject_peekedIsPeekedEndObject_decrementsStackAndClearsPathNameAndIncrementsPathIndex() throws Exception {
    // Use reflection to set private fields
    setField(jsonReader, "peeked", 2); // PEEKED_END_OBJECT = 2
    setField(jsonReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[1] = "foo";
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = 5;
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.endObject();

    int stackSizeAfter = (int) getField(jsonReader, "stackSize");
    assertEquals(1, stackSizeAfter);

    String[] pathNamesAfter = (String[]) getField(jsonReader, "pathNames");
    assertNull(pathNamesAfter[1]);

    int[] pathIndicesAfter = (int[]) getField(jsonReader, "pathIndices");
    assertEquals(6, pathIndicesAfter[0]);

    int peekedAfter = (int) getField(jsonReader, "peeked");
    assertEquals(0, peekedAfter); // PEEKED_NONE = 0
  }

  @Test
    @Timeout(8000)
  public void endObject_peekedIsPeekedNone_callsDoPeekAndBehavesCorrectly() throws Exception {
    // Set peeked to PEEKED_NONE so doPeek() will be called
    setField(jsonReader, "peeked", 0);

    // Spy on jsonReader to mock doPeek()
    JsonReader spyReader = spy(jsonReader);

    // Mock doPeek() to return PEEKED_END_OBJECT
    doReturn(2).when(spyReader).doPeek();

    // Setup stackSize, pathNames, pathIndices for after successful endObject
    setField(spyReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[1] = "bar";
    setField(spyReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = 10;
    setField(spyReader, "pathIndices", pathIndices);

    spyReader.endObject();

    int stackSizeAfter = (int) getField(spyReader, "stackSize");
    assertEquals(1, stackSizeAfter);

    String[] pathNamesAfter = (String[]) getField(spyReader, "pathNames");
    assertNull(pathNamesAfter[1]);

    int[] pathIndicesAfter = (int[]) getField(spyReader, "pathIndices");
    assertEquals(11, pathIndicesAfter[0]);

    int peekedAfter = (int) getField(spyReader, "peeked");
    assertEquals(0, peekedAfter);

    // Verify doPeek was called
    verify(spyReader).doPeek();
  }

  @Test
    @Timeout(8000)
  public void endObject_peekedIsNotEndObject_throwsIllegalStateException() throws Exception {
    // Set peeked to a value other than PEEKED_END_OBJECT or PEEKED_NONE
    setField(jsonReader, "peeked", 1); // PEEKED_BEGIN_OBJECT

    // Spy to mock peek() and locationString()
    JsonReader spyReader = spy(jsonReader);

    when(spyReader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    when(spyReader.locationString()).thenReturn(" at path $");

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::endObject);
    assertTrue(ex.getMessage().contains("Expected END_OBJECT but was BEGIN_OBJECT at path $"));
  }

  // Helper to set private field
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private field
  private Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}