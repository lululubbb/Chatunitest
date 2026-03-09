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

class JsonReader_204_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void nextLong_peekedLong_returnsPeekedLong() throws Exception {
    setField(jsonReader, "peeked", 15); // PEEKED_LONG
    setField(jsonReader, "peekedLong", 123456789L);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    long result = jsonReader.nextLong();

    assertEquals(123456789L, result);
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextLong_peekedNumber_parsesLongFromBuffer() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    char[] buffer = "12345abcde".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 5);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    long result = jsonReader.nextLong();

    assertEquals(12345L, result);
    assertEquals(5, getField(jsonReader, "pos"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
    assertNull(getField(jsonReader, "peekedString"));
  }

  @Test
    @Timeout(8000)
  void nextLong_peekedSingleQuoted_parsesLongFromQuotedValue() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Mock nextQuotedValue to return "987654321"
    Method nextQuotedValue = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValue.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn("987654321").when(spyReader).nextQuotedValue('\'');

    long result = spyReader.nextLong();

    assertEquals(987654321L, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextLong_peekedDoubleQuoted_parsesLongFromQuotedValue() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("1234567890").when(spyReader).nextQuotedValue('"');

    long result = spyReader.nextLong();

    assertEquals(1234567890L, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextLong_peekedUnquoted_parsesLongFromUnquotedValue() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("456789").when(spyReader).nextUnquotedValue();

    long result = spyReader.nextLong();

    assertEquals(456789L, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextLong_peekedUnquoted_invalidLongFallsBackToDouble() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("123.0").when(spyReader).nextUnquotedValue();

    long result = spyReader.nextLong();

    assertEquals(123L, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextLong_peekedUnquoted_invalidDoubleThrowsNumberFormatException() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn("123.456").when(spyReader).nextUnquotedValue();

    // This should throw because 123.456 cast to long loses precision
    Exception ex = assertThrows(NumberFormatException.class, spyReader::nextLong);
    assertTrue(ex.getMessage().contains("Expected a long but was 123.456"));
  }

  @Test
    @Timeout(8000)
  void nextLong_invalidPeeked_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE, invalid for nextLong
    Exception ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextLong());
    assertTrue(ex.getMessage().contains("Expected a long but was"));
  }

  @Test
    @Timeout(8000)
  void nextLong_peekedNone_invokesDoPeek() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn(15).when(spyReader).doPeek();
    setField(spyReader, "peekedLong", 999L);

    long result = spyReader.nextLong();

    assertEquals(999L, result);
    verify(spyReader).doPeek();
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  // Helper methods for reflection
  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    T val = (T) field.get(target);
    return val;
  }
}