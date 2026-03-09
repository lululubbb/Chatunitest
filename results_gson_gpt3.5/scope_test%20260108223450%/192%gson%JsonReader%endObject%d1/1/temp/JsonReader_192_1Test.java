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

public class JsonReader_192_1Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() throws Exception {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Initialize stackSize and arrays for pathNames and pathIndices
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);

    Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = new String[32];
    pathNames[0] = "name";
    pathNamesField.set(jsonReader, pathNames);

    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndicesField.set(jsonReader, pathIndices);
  }

  @Test
    @Timeout(8000)
  public void testEndObject_whenPeekedIsPeekedEndObject_shouldDecrementStackSizeAndUpdateFields() throws Exception {
    // Set peeked to PEEKED_END_OBJECT
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    peekedField.setInt(jsonReader, 2); // PEEKED_END_OBJECT
    stackSizeField.setInt(jsonReader, 1);

    String[] pathNames = (String[]) pathNamesField.get(jsonReader);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    pathNames[0] = "lastName";
    pathIndices[0] = 5;

    jsonReader.endObject();

    int stackSizeAfter = stackSizeField.getInt(jsonReader);
    String[] pathNamesAfter = (String[]) pathNamesField.get(jsonReader);
    int[] pathIndicesAfter = (int[]) pathIndicesField.get(jsonReader);
    int peekedAfter = peekedField.getInt(jsonReader);

    assertEquals(0, stackSizeAfter, "stackSize should be decremented");
    assertNull(pathNamesAfter[0], "pathNames at old stackSize should be null");
    assertEquals(6, pathIndicesAfter[0], "pathIndices at stackSize-1 should be incremented");
    assertEquals(0, peekedAfter, "peeked should be reset to PEEKED_NONE");
  }

  @Test
    @Timeout(8000)
  public void testEndObject_whenPeekedIsPeekedNoneAndDoPeekReturnsPeekedEndObject_shouldBehaveAsExpected() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, 0); // PEEKED_NONE

    // Mock doPeek to return PEEKED_END_OBJECT
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(2).when(spyReader).doPeek(); // PEEKED_END_OBJECT

    // Setup stackSize and arrays
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);
    Field pathNamesField = JsonReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = new String[32];
    pathNames[0] = "foo";
    pathNamesField.set(spyReader, pathNames);
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 10;
    pathIndicesField.set(spyReader, pathIndices);

    spyReader.endObject();

    int stackSizeAfter = stackSizeField.getInt(spyReader);
    String[] pathNamesAfter = (String[]) pathNamesField.get(spyReader);
    int[] pathIndicesAfter = (int[]) pathIndicesField.get(spyReader);
    int peekedAfter = peekedField.getInt(spyReader);

    assertEquals(0, stackSizeAfter);
    assertNull(pathNamesAfter[0]);
    assertEquals(11, pathIndicesAfter[0]);
    assertEquals(0, peekedAfter);
  }

  @Test
    @Timeout(8000)
  public void testEndObject_whenPeekedIsNotEndObject_shouldThrowIllegalStateException() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, 5); // PEEKED_TRUE (not end object)

    // Mock peek() and locationString() to avoid NullPointerException in message
    JsonReader spyReader = spy(jsonReader);
    // Correct enum constant is TRUE (all uppercase)
    doReturn(Enum.valueOf((Class<Enum>) Class.forName("com.google.gson.stream.JsonToken"), "TRUE")).when(spyReader).peek();
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::endObject);
    assertTrue(ex.getMessage().contains("Expected END_OBJECT but was TRUE at path $"));
  }
}