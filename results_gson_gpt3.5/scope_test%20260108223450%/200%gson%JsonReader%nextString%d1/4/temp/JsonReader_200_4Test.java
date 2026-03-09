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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JsonReader_200_4Test {

  JsonReader jsonReader;
  Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize to 1 to avoid ArrayIndexOutOfBounds in nextString()
    setField(jsonReader, "stackSize", 1);
    // Initialize pathIndices array element for stackSize-1
    int[] pathIndices = (int[]) getField(jsonReader, "pathIndices");
    pathIndices[0] = 0;
  }

  @Test
    @Timeout(8000)
  void nextString_peekedNone_callsDoPeek_andHandlesUnquoted() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    // Mock doPeek() to return PEEKED_UNQUOTED
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    JsonReader spyReader = Mockito.spy(jsonReader);
    doPeekMethod.invoke(spyReader);
    when(spyReader.doPeek()).thenReturn(10); // PEEKED_UNQUOTED

    // Stub nextUnquotedValue() private method
    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);
    JsonReader spy = Mockito.spy(jsonReader);
    doReturn("unquotedValue").when(spy).nextUnquotedValue();

    setField(spy, "peeked", 0);
    setField(spy, "stackSize", 1);
    int[] pathIndices = (int[]) getField(spy, "pathIndices");
    pathIndices[0] = 0;

    String result = spy.nextString();

    assertEquals("unquotedValue", result);
    assertEquals(0, getField(spy, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedSingleQuoted_returnsNextQuotedValue() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    Method nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);

    JsonReader spy = Mockito.spy(jsonReader);
    doReturn("singleQuotedValue").when(spy).nextQuotedValue('\'');

    setField(spy, "stackSize", 1);
    int[] pathIndices = (int[]) getField(spy, "pathIndices");
    pathIndices[0] = 0;

    String result = spy.nextString();

    assertEquals("singleQuotedValue", result);
    assertEquals(0, getField(spy, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedDoubleQuoted_returnsNextQuotedValue() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED

    JsonReader spy = Mockito.spy(jsonReader);
    doReturn("doubleQuotedValue").when(spy).nextQuotedValue('"');

    setField(spy, "stackSize", 1);
    int[] pathIndices = (int[]) getField(spy, "pathIndices");
    pathIndices[0] = 0;

    String result = spy.nextString();

    assertEquals("doubleQuotedValue", result);
    assertEquals(0, getField(spy, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedBuffered_returnsPeekedString() throws Exception {
    setField(jsonReader, "peeked", 11); // PEEKED_BUFFERED
    setField(jsonReader, "peekedString", "bufferedValue");

    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = (int[]) getField(jsonReader, "pathIndices");
    pathIndices[0] = 0;

    String result = jsonReader.nextString();

    assertEquals("bufferedValue", result);
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedLong_returnsStringOfPeekedLong() throws Exception {
    setField(jsonReader, "peeked", 15); // PEEKED_LONG
    setField(jsonReader, "peekedLong", 123456789L);

    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = (int[]) getField(jsonReader, "pathIndices");
    pathIndices[0] = 0;

    String result = jsonReader.nextString();

    assertEquals("123456789", result);
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_peekedNumber_returnsStringFromBuffer() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    char[] buffer = (char[]) getField(jsonReader, "buffer");
    String numberStr = "31415";
    for (int i = 0; i < numberStr.length(); i++) {
      buffer[i] = numberStr.charAt(i);
    }
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", numberStr.length());

    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = (int[]) getField(jsonReader, "pathIndices");
    pathIndices[0] = 0;

    String result = jsonReader.nextString();

    assertEquals(numberStr, result);
    assertEquals(numberStr.length(), getField(jsonReader, "pos"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextString_invalidPeeked_throwsIllegalStateException() {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (invalid for nextString)
    setField(jsonReader, "stackSize", 1);
    Exception exception = assertThrows(IllegalStateException.class, () -> jsonReader.nextString());
    assertTrue(exception.getMessage().contains("Expected a string but was"));
  }

  // Helper methods to set and get private fields via reflection

  private static void setField(Object target, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Object getField(Object target, String fieldName) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}