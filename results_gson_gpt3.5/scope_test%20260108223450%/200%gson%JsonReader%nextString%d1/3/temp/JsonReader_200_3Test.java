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

class JsonReader_200_3Test {

  JsonReader jsonReader;
  Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedNone_callsDoPeekAndReturnsUnquotedValue() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE = 0
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    // Mock doPeek to return PEEKED_UNQUOTED (10)
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doPeek.invoke(spyReader);

    // Override doPeek to return PEEKED_UNQUOTED
    doReturn(10).when(spyReader).doPeek();

    // Override nextUnquotedValue to return "unquoted"
    doReturn("unquoted").when(spyReader).nextUnquotedValue();

    String result = spyReader.nextString();

    assertEquals("unquoted", result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedSingleQuoted_returnsNextQuotedValueWithSingleQuote() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED = 8
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn("single-quoted").when(spyReader).nextQuotedValue('\'');

    String result = spyReader.nextString();

    assertEquals("single-quoted", result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedDoubleQuoted_returnsNextQuotedValueWithDoubleQuote() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED = 9
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    JsonReader spyReader = spy(jsonReader);
    doReturn("double-quoted").when(spyReader).nextQuotedValue('"');

    String result = spyReader.nextString();

    assertEquals("double-quoted", result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedBuffered_returnsPeekedStringAndNullsIt() throws Exception {
    setField(jsonReader, "peeked", 11); // PEEKED_BUFFERED = 11
    setField(jsonReader, "peekedString", "bufferedString");
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    String result = jsonReader.nextString();

    assertEquals("bufferedString", result);
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedLong_returnsLongToString() throws Exception {
    setField(jsonReader, "peeked", 15); // PEEKED_LONG = 15
    setField(jsonReader, "peekedLong", 123456789L);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    String result = jsonReader.nextString();

    assertEquals("123456789", result);
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedNumber_returnsStringFromBufferAndUpdatesPos() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER = 16
    setField(jsonReader, "peekedNumberLength", 3);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    char[] buffer = new char[1024];
    buffer[0] = '1';
    buffer[1] = '2';
    buffer[2] = '3';
    setField(jsonReader, "buffer", buffer);

    String result = jsonReader.nextString();

    assertEquals("123", result);
    assertEquals(3, getField(jsonReader, "pos"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, ((int[]) getField(jsonReader, "pathIndices"))[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_invalidPeeked_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE = 5 (not a string)
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    Exception exception = assertThrows(IllegalStateException.class, () -> jsonReader.nextString());
    String message = exception.getMessage();
    assertTrue(message.startsWith("Expected a string but was"));
  }

  // Utility methods for reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T getField(Object target, String fieldName) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}