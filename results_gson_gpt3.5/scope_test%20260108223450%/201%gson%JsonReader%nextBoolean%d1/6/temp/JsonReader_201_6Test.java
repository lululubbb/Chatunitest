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

public class JsonReader_201_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_peekedIsPEEKED_TRUE_returnsTrueAndUpdatesState() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 5); // PEEKED_TRUE
    setPathIndicesField(JsonReader.class, jsonReader, new int[] {0});
    setStackSizeField(JsonReader.class, jsonReader, 1);

    boolean result = jsonReader.nextBoolean();

    assertTrue(result);
    assertEquals(0, getPeekedField(JsonReader.class, jsonReader));
    assertEquals(1, getPathIndicesField(JsonReader.class, jsonReader)[0]);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_peekedIsPEEKED_FALSE_returnsFalseAndUpdatesState() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 6); // PEEKED_FALSE
    setPathIndicesField(JsonReader.class, jsonReader, new int[] {5});
    setStackSizeField(JsonReader.class, jsonReader, 1);

    boolean result = jsonReader.nextBoolean();

    assertFalse(result);
    assertEquals(0, getPeekedField(JsonReader.class, jsonReader));
    assertEquals(6, getPathIndicesField(JsonReader.class, jsonReader)[0]);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_peekedIsPEEKED_NONE_callsDoPeekAndReturnsTrue() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 0); // PEEKED_NONE
    setPathIndicesField(JsonReader.class, jsonReader, new int[] {2});
    setStackSizeField(JsonReader.class, jsonReader, 1);

    // Spy to mock doPeek() and peek()
    JsonReader spyReader = spy(jsonReader);
    doReturn(5).when(spyReader).doPeek();

    boolean result = spyReader.nextBoolean();

    assertTrue(result);
    assertEquals(0, getPeekedField(JsonReader.class, spyReader));
    assertEquals(3, getPathIndicesField(JsonReader.class, spyReader)[0]);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_peekedIsPEEKED_NONE_callsDoPeekAndReturnsFalse() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 0); // PEEKED_NONE
    setPathIndicesField(JsonReader.class, jsonReader, new int[] {3});
    setStackSizeField(JsonReader.class, jsonReader, 1);

    // Spy to mock doPeek() and peek()
    JsonReader spyReader = spy(jsonReader);
    doReturn(6).when(spyReader).doPeek();

    boolean result = spyReader.nextBoolean();

    assertFalse(result);
    assertEquals(0, getPeekedField(JsonReader.class, spyReader));
    assertEquals(4, getPathIndicesField(JsonReader.class, spyReader)[0]);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_invalidPeeked_throwsIllegalStateException() throws Exception {
    setPeekedField(JsonReader.class, jsonReader, 1); // PEEKED_BEGIN_OBJECT
    setStackSizeField(JsonReader.class, jsonReader, 1);

    // Spy to mock peek() and locationString()
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::nextBoolean);
    assertTrue(ex.getMessage().contains("Expected a boolean but was BEGIN_OBJECT at path $"));
  }

  // Helper methods for reflection to access private fields and methods

  private static void setPeekedField(Class<?> clazz, Object instance, int value) throws Exception {
    Field peekedField = clazz.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(instance, value);
  }

  private static int getPeekedField(Class<?> clazz, Object instance) throws Exception {
    Field peekedField = clazz.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(instance);
  }

  private static void setPathIndicesField(Class<?> clazz, Object instance, int[] value) throws Exception {
    Field pathIndicesField = clazz.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    pathIndicesField.set(instance, value);
  }

  private static int[] getPathIndicesField(Class<?> clazz, Object instance) throws Exception {
    Field pathIndicesField = clazz.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    return (int[]) pathIndicesField.get(instance);
  }

  private static void setStackSizeField(Class<?> clazz, Object instance, int value) throws Exception {
    Field stackSizeField = clazz.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(instance, value);
  }

}