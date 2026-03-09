package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_192_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  private static final int PEEKED_NONE = 0;
  private static final int PEEKED_BEGIN_OBJECT = 1;
  private static final int PEEKED_END_OBJECT = 2;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void endObject_peekedIsPeekedEndObject_decrementsStackAndUpdatesState() throws Exception {
    setField(jsonReader, "peeked", PEEKED_END_OBJECT);
    setField(jsonReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[1] = "name";
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.endObject();

    assertEquals(1, (int) getField(jsonReader, "stackSize"));
    assertNull(pathNames[1]);
    assertEquals(1, pathIndices[0]);
    assertEquals(PEEKED_NONE, (int) getField(jsonReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  void endObject_peekedIsPeekedNone_callsDoPeekAndProcesses() throws Exception {
    setField(jsonReader, "peeked", PEEKED_NONE);
    // We will spy jsonReader to mock doPeek and peek
    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[1] = "name";
    setField(spyReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(spyReader, "pathIndices", pathIndices);

    doReturn(PEEKED_END_OBJECT).when(spyReader).doPeek();

    spyReader.endObject();

    assertEquals(1, (int) getField(spyReader, "stackSize"));
    assertNull(pathNames[1]);
    assertEquals(1, pathIndices[0]);
    assertEquals(PEEKED_NONE, (int) getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  void endObject_peekedIsNotEndObject_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", PEEKED_NONE);
    JsonReader spyReader = spy(jsonReader);

    doReturn(PEEKED_BEGIN_OBJECT).when(spyReader).doPeek();
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    // locationString() returns a String, invoke private method via reflection
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    String locStr = (String) locationStringMethod.invoke(spyReader);
    String expectedMessage = "Expected END_OBJECT but was " + JsonToken.BEGIN_OBJECT + locStr;

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::endObject);
    assertEquals(expectedMessage, thrown.getMessage());
  }

  // Helper methods to set and get private fields via reflection
  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}