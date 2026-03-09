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

public class JsonReader_203_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedNone_callsDoPeekAndParsesDouble() throws Exception {
    // Arrange
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    // We mock doPeek to return PEEKED_NUMBER (16)
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(16).when(spyReader).doPeek();

    // Setup fields for PEEKED_NUMBER branch
    char[] buffer = "123.456extra".toCharArray();
    setField(spyReader, "buffer", buffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "peekedNumberLength", 7); // "123.456"
    setField(spyReader, "peeked", 16);
    setField(spyReader, "pathIndices", new int[32]);
    setField(spyReader, "stackSize", 1);
    setField(spyReader, "lenient", false);

    // Act
    double result = spyReader.nextDouble();

    // Assert
    assertEquals(123.456, result, 0.000001);
    assertEquals(7, getField(spyReader, "pos"));
    assertEquals(0, getField(spyReader, "peeked"));
    assertNull(getField(spyReader, "peekedString"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedLong_returnsPeekedLongAsDouble() throws IOException {
    setField(jsonReader, "peeked", 15); // PEEKED_LONG
    setField(jsonReader, "peekedLong", 42L);
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "stackSize", 1);

    double result = jsonReader.nextDouble();

    assertEquals(42.0, result, 0.0);
    assertEquals(0, getField(jsonReader, "peeked"));
    int[] pathIndices = getField(jsonReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedSingleQuoted_returnsParsedDouble() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "lenient", false);

    JsonReader spyReader = spy(jsonReader);
    doReturn("3.14").when(spyReader).nextQuotedValue('\'');

    double result = spyReader.nextDouble();

    assertEquals(3.14, result, 0.000001);
    assertEquals(0, getField(spyReader, "peeked"));
    assertNull(getField(spyReader, "peekedString"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedDoubleQuoted_returnsParsedDouble() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "lenient", false);

    JsonReader spyReader = spy(jsonReader);
    doReturn("2.718").when(spyReader).nextQuotedValue('"');

    double result = spyReader.nextDouble();

    assertEquals(2.718, result, 0.000001);
    assertEquals(0, getField(spyReader, "peeked"));
    assertNull(getField(spyReader, "peekedString"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedUnquoted_returnsParsedDouble() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "lenient", false);

    JsonReader spyReader = spy(jsonReader);
    doReturn("0.57721").when(spyReader).nextUnquotedValue();

    double result = spyReader.nextDouble();

    assertEquals(0.57721, result, 0.000001);
    assertEquals(0, getField(spyReader, "peeked"));
    assertNull(getField(spyReader, "peekedString"));
    int[] pathIndices = getField(spyReader, "pathIndices");
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void nextDouble_peekedInvalid_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (invalid for double)
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.TRUE).when(spyReader).peek();
    doReturn(" at path $").when(spyReader, "locationString");

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::nextDouble);
    assertTrue(ex.getMessage().contains("Expected a double but was TRUE"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_nanOrInfiniteLenientFalse_throwsMalformedJsonException() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "lenient", false);

    JsonReader spyReader = spy(jsonReader);
    doReturn("NaN").when(spyReader).nextUnquotedValue();
    doReturn(" at path $").when(spyReader, "locationString");

    MalformedJsonException ex = assertThrows(MalformedJsonException.class, spyReader::nextDouble);
    assertTrue(ex.getMessage().contains("JSON forbids NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  void nextDouble_nanOrInfiniteLenientTrue_allowsResult() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "pathIndices", new int[32]);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "lenient", true);

    JsonReader spyReader = spy(jsonReader);
    doReturn("NaN").when(spyReader).nextUnquotedValue();

    double result = spyReader.nextDouble();

    assertTrue(Double.isNaN(result));
    assertEquals(0, getField(spyReader, "peeked"));
  }

  // Helper to set private fields via reflection
  private static <T> void setField(Object target, String fieldName, T value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get private fields via reflection
  @SuppressWarnings("unchecked")
  private static <T> T getField(Object target, String fieldName) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}