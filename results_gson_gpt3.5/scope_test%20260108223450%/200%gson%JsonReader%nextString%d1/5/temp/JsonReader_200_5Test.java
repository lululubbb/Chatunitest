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

class JsonReader_200_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and pathIndices for pathIndices[stackSize - 1]++
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedNone_callsDoPeekAndReturnsUnquoted() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    // Mock doPeek to return PEEKED_UNQUOTED
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(10).when(spyReader).doPeek(); // PEEKED_UNQUOTED

    // Mock nextUnquotedValue to return "unquotedValue"
    Method nextUnquotedValue = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValue.setAccessible(true);
    doReturn("unquotedValue").when(spyReader).nextUnquotedValue();

    String result = spyReader.nextString();

    assertEquals("unquotedValue", result);
    assertEquals(0, getField(spyReader, "peeked"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedUnquoted_returnsNextUnquotedValue() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED

    Method nextUnquotedValue = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValue.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn("unquotedValue").when(spyReader).nextUnquotedValue();

    String result = spyReader.nextString();

    assertEquals("unquotedValue", result);
    assertEquals(0, getField(spyReader, "peeked"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedSingleQuoted_returnsNextQuotedValue() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED

    JsonReader spyReader = spy(jsonReader);
    doReturn("singleQuotedValue").when(spyReader).nextQuotedValue('\'');

    String result = spyReader.nextString();

    assertEquals("singleQuotedValue", result);
    assertEquals(0, getField(spyReader, "peeked"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedDoubleQuoted_returnsNextQuotedValue() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED

    JsonReader spyReader = spy(jsonReader);
    doReturn("doubleQuotedValue").when(spyReader).nextQuotedValue('"');

    String result = spyReader.nextString();

    assertEquals("doubleQuotedValue", result);
    assertEquals(0, getField(spyReader, "peeked"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedBuffered_returnsPeekedStringAndNullsIt() throws Exception {
    setField(jsonReader, "peeked", 11); // PEEKED_BUFFERED
    setField(jsonReader, "peekedString", "bufferedString");

    String result = jsonReader.nextString();

    assertEquals("bufferedString", result);
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    int[] pathIndices = getField(jsonReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedLong_returnsLongAsString() throws Exception {
    setField(jsonReader, "peeked", 15); // PEEKED_LONG
    setField(jsonReader, "peekedLong", 123456789L);

    String result = jsonReader.nextString();

    assertEquals("123456789", result);
    assertEquals(0, getField(jsonReader, "peeked"));
    int[] pathIndices = getField(jsonReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedNumber_returnsStringFromBufferAndAdvancesPos() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    setField(jsonReader, "peekedNumberLength", 3);
    setField(jsonReader, "pos", 1);
    char[] buffer = new char[1024];
    buffer[1] = '1';
    buffer[2] = '2';
    buffer[3] = '3';
    setField(jsonReader, "buffer", buffer);

    String result = jsonReader.nextString();

    assertEquals("123", result);
    assertEquals(4, getField(jsonReader, "pos"));
    assertEquals(0, getField(jsonReader, "peeked"));
    int[] pathIndices = getField(jsonReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_invalidPeeked_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (not a string)
    // We must mock peek() and locationString() to avoid NPE and get meaningful message
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BOOLEAN).when(spyReader).peek();
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException e = assertThrows(IllegalStateException.class, spyReader::nextString);
    assertTrue(e.getMessage().contains("Expected a string but was BOOLEAN at path $"));
  }

  // Helpers for reflection

  @SuppressWarnings("unchecked")
  private <T> T getField(Object obj, String name) {
    try {
      Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      return (T) field.get(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setField(Object obj, String name, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(name);
      field.setAccessible(true);
      field.set(obj, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}