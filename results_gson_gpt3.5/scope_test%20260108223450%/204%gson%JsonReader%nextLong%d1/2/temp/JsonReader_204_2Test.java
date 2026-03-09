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

public class JsonReader_204_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedLong() throws Exception {
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
  public void testNextLong_peekedNumber() throws Exception {
    setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
    char[] buffer = new char[1024];
    String numberStr = "9876543210";
    for (int i = 0; i < numberStr.length(); i++) {
      buffer[i] = numberStr.charAt(i);
    }
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "peekedNumberLength", numberStr.length());
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    long result = jsonReader.nextLong();

    assertEquals(9876543210L, result);
    assertEquals(numberStr.length(), getField(jsonReader, "pos"));
    assertNull(getField(jsonReader, "peekedString"));
    assertEquals(0, getField(jsonReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedSingleQuoted_validLong() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    // Mock nextQuotedValue to return a valid long string
    JsonReader spyReader = spy(jsonReader);
    doReturn("123456").when(spyReader).nextQuotedValue('\'');

    long result = spyReader.nextLong();

    assertEquals(123456L, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedDoubleQuoted_validLong() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("654321").when(spyReader).nextQuotedValue('"');

    long result = spyReader.nextLong();

    assertEquals(654321L, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedUnquoted_validLong() throws Exception {
    setField(jsonReader, "peeked", 10); // PEEKED_UNQUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    doReturn("7890").when(spyReader).nextUnquotedValue();

    long result = spyReader.nextLong();

    assertEquals(7890L, result);
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedQuoted_invalidLongButValidDouble() throws Exception {
    setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    // Return a string that is not a valid long but valid double with integer value
    doReturn("123456.0").when(spyReader).nextQuotedValue('\'');

    long result = spyReader.nextLong();

    assertEquals(123456L, result);
    assertNull(getField(spyReader, "peekedString"));
    assertEquals(0, getField(spyReader, "peeked"));
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextLong_peekedQuoted_invalidLongAndDoublePrecisionLoss() throws Exception {
    setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
    setField(jsonReader, "stackSize", 1);
    int[] pathIndices = new int[32];
    setField(jsonReader, "pathIndices", pathIndices);

    JsonReader spyReader = spy(jsonReader);
    // Return a double string that loses precision when cast to long
    doReturn("123456.789").when(spyReader).nextQuotedValue('"');

    NumberFormatException thrown = assertThrows(NumberFormatException.class, spyReader::nextLong);
    assertTrue(thrown.getMessage().contains("Expected a long but was"));
  }

  @Test
    @Timeout(8000)
  public void testNextLong_illegalStateException() throws Exception {
    setField(jsonReader, "peeked", 5); // PEEKED_TRUE (not a number)
    setField(jsonReader, "stackSize", 1);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonReader.nextLong());
    assertTrue(thrown.getMessage().startsWith("Expected a long but was"));
  }

  // Helper methods to access private fields via reflection
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}