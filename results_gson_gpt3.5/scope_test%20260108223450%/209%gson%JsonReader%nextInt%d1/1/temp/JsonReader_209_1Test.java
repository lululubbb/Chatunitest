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

public class JsonReader_209_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and pathIndices for coverage
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedNone_callsDoPeekAndReturnsIntFromPeekedLong() throws Exception {
    setField(jsonReader, "peeked", 0); // PEEKED_NONE
    setField(jsonReader, "peekedLong", 123);
    setField(jsonReader, "peeked", 15); // PEEKED_LONG
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Spy on jsonReader to mock doPeek
    JsonReader spyReader = spy(jsonReader);
    doReturn(15).when(spyReader).doPeek();

    int result = spyReader.nextInt();

    assertEquals(123, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedLong_precisionLoss_throwsNumberFormatException() throws Exception {
    setField(jsonReader, "peeked", 15); // PEEKED_LONG
    setField(jsonReader, "peekedLong", ((long) Integer.MAX_VALUE) + 1);
    setField(jsonReader, "stackSize", 1);
    setField(jsonReader, "pathIndices", new int[32]);

    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedNumber_parsesIntSuccessfully() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    char[] buffer = "12345".toCharArray();
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", 5);
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    int result = jsonReader.nextInt();

    assertEquals(12345, result);
    assertEquals(5, getField(jsonReader, "pos"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedSingleQuoted_parsesIntSuccessfully() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("42").when(spyReader).nextQuotedValue('\'');
    int result = spyReader.nextInt();

    assertEquals(42, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedDoubleQuoted_parsesIntSuccessfully() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("123").when(spyReader).nextQuotedValue('"');
    int result = spyReader.nextInt();

    assertEquals(123, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedUnquoted_parsesIntSuccessfully() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("7").when(spyReader).nextUnquotedValue();
    int result = spyReader.nextInt();

    assertEquals(7, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedUnquoted_parseIntFails_parseDoubleSuccessfully() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("7.0").when(spyReader).nextUnquotedValue();

    int result = spyReader.nextInt();

    assertEquals(7, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void nextInt_peekedUnquoted_parseIntFails_parseDoublePrecisionLoss_throwsNumberFormatException() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("7.1").when(spyReader).nextUnquotedValue();

    NumberFormatException ex = assertThrows(NumberFormatException.class, spyReader::nextInt);
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  @Test
    @Timeout(8000)
  public void nextInt_invalidPeek_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (not handled in nextInt)
    setField(jsonReader, "stackSize", 1);
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextInt());
    assertTrue(ex.getMessage().contains("Expected an int but was"));
  }

  // Helper methods to access private fields via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
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