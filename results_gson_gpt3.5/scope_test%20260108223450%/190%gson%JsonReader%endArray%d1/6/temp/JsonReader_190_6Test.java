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

class JsonReader_190_6Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() throws Exception {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Set stackSize to 2 and initialize pathIndices to test increment
    setField(jsonReader, "stackSize", 2);
    int[] pathIndices = new int[32];
    pathIndices[0] = 5; // changed to pathIndices[0] = 5 to match stackSize-1 indexing after endArray decrements
    setField(jsonReader, "pathIndices", pathIndices);
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsPEEKED_END_ARRAY_decrementsStackSizeAndIncrementsPathIndexAndResetsPeeked() throws Exception {
    setField(jsonReader, "peeked", getPeekedEndArray());
    int originalStackSize = (int) getField(jsonReader, "stackSize");
    int[] pathIndices = (int[]) getField(jsonReader, "pathIndices");
    int originalPathIndex = pathIndices[originalStackSize - 2]; // pathIndices at stackSize-2 before endArray

    jsonReader.endArray();

    int newStackSize = (int) getField(jsonReader, "stackSize");
    int[] updatedPathIndices = (int[]) getField(jsonReader, "pathIndices");
    int peeked = (int) getField(jsonReader, "peeked");

    assertEquals(originalStackSize - 1, newStackSize, "stackSize should be decremented");
    assertEquals(originalPathIndex + 1, updatedPathIndices[newStackSize - 1], "pathIndices at new stackSize-1 should be incremented");
    assertEquals(getPeekedNone(), peeked, "peeked should be reset to PEEKED_NONE");
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsPEEKED_NONE_callsDoPeekAndThrowsIfNotPEEKED_END_ARRAY() throws Exception {
    setField(jsonReader, "peeked", getPeekedNone());

    // Spy on jsonReader to mock doPeek and peek methods
    JsonReader spyReader = spy(jsonReader);

    // doPeek returns a code not equal to PEEKED_END_ARRAY (e.g. PEEKED_BEGIN_ARRAY)
    doReturn(getPeekedBeginArray()).when(spyReader).doPeek();
    // peek returns a token for exception message
    doReturn(JsonToken.BEGIN_ARRAY).when(spyReader).peek();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::endArray);
    assertTrue(thrown.getMessage().contains("Expected END_ARRAY but was BEGIN_ARRAY"));
  }

  @Test
    @Timeout(8000)
  void endArray_peekedIsInvalid_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", getPeekedBeginObject());

    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::endArray);
    assertTrue(thrown.getMessage().contains("Expected END_ARRAY but was BEGIN_OBJECT"));
  }

  // Helper to set private field value via reflection
  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private field value via reflection
  private static Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  // Constants from JsonReader class
  private static int getPeekedNone() throws Exception {
    return getStaticFinalIntField("PEEKED_NONE");
  }

  private static int getPeekedEndArray() throws Exception {
    return getStaticFinalIntField("PEEKED_END_ARRAY");
  }

  private static int getPeekedBeginArray() throws Exception {
    return getStaticFinalIntField("PEEKED_BEGIN_ARRAY");
  }

  private static int getPeekedBeginObject() throws Exception {
    return getStaticFinalIntField("PEEKED_BEGIN_OBJECT");
  }

  private static int getStaticFinalIntField(String name) throws Exception {
    Field field = JsonReader.class.getDeclaredField(name);
    field.setAccessible(true);
    return field.getInt(null);
  }
}