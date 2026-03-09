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

class JsonReader_192_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void endObject_peekedIsPeekedEndObject_shouldUpdateState() throws Exception {
    setField(jsonReader, "peeked", 2); // PEEKED_END_OBJECT
    setField(jsonReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[1] = "name";
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = 5;
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.endObject();

    assertEquals(1, (int) getField(jsonReader, "stackSize"));
    assertNull(pathNames[1]);
    assertEquals(6, pathIndices[0]);
    assertEquals(0, (int) getField(jsonReader, "peeked")); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void endObject_peekedIsPeekedNoneAndDoPeekReturnsEndObject_shouldUpdateState() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    setField(jsonReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[1] = "lastName";
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = 3;
    setField(jsonReader, "pathIndices", pathIndices);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(2).when(spyReader).doPeek(); // PEEKED_END_OBJECT

    spyReader.endObject();

    assertEquals(1, (int) getField(spyReader, "stackSize"));
    assertNull(pathNames[1]);
    assertEquals(4, pathIndices[0]);
    assertEquals(0, (int) getField(spyReader, "peeked"));
  }

  @Test
    @Timeout(8000)
  void endObject_peekedIsNotEndObject_shouldThrowIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 1); // PEEKED_BEGIN_OBJECT which is invalid here

    Method peek = JsonReader.class.getDeclaredMethod("peek");
    peek.setAccessible(true);

    Exception exception = assertThrows(IllegalStateException.class, () -> jsonReader.endObject());
    String message = exception.getMessage();
    assertTrue(message.startsWith("Expected END_OBJECT but was "));
  }

  // Helper to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private fields via reflection
  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}