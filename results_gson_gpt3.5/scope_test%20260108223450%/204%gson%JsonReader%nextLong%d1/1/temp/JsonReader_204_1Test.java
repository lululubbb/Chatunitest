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
import org.mockito.Mockito;

class JsonReader_204_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and pathIndices for coverage
    try {
      Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonReader, 1);

      Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
      pathIndicesField.setAccessible(true);
      int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
      pathIndices[0] = 0;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setPeeked(int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private int getPeeked() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(jsonReader);
  }

  private void setPeekedLong(long value) throws Exception {
    Field peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
    peekedLongField.setAccessible(true);
    peekedLongField.setLong(jsonReader, value);
  }

  private void setPeekedNumberLength(int value) throws Exception {
    Field peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
    peekedNumberLengthField.setAccessible(true);
    peekedNumberLengthField.setInt(jsonReader, value);
  }

  private void setPos(int value) throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, value);
  }

  private void setPeekedString(String value) throws Exception {
    Field peekedStringField = JsonReader.class.getDeclaredField("peekedString");
    peekedStringField.setAccessible(true);
    peekedStringField.set(jsonReader, value);
  }

  private void setBuffer(char[] buffer) throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, buffer);
  }

  private void incrementPathIndex() throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    pathIndices[0]++;
  }

  private int getPathIndex() throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    return pathIndices[0];
  }

  private int invokeDoPeek() throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    return (int) doPeekMethod.invoke(jsonReader);
  }

  private String invokeNextUnquotedValue() throws Exception {
    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);
    return (String) nextUnquotedValueMethod.invoke(jsonReader);
  }

  private String invokeNextQuotedValue(char quote) throws Exception {
    Method nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);
    return (String) nextQuotedValueMethod.invoke(jsonReader, quote);
  }

  private JsonToken invokePeek() throws Exception {
    Method peekMethod = JsonReader.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    return (JsonToken) peekMethod.invoke(jsonReader);
  }

  private String invokeLocationString() throws Exception {
    Method locationStringMethod = JsonReader.class.getDeclaredMethod("locationString");
    locationStringMethod.setAccessible(true);
    return (String) locationStringMethod.invoke(jsonReader);
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedLong() throws Exception {
    setPeeked(JsonReader.PEEKED_LONG);
    setPeekedLong(123456789L);
    int beforeIndex = getPathIndex();

    long result = jsonReader.nextLong();

    assertEquals(123456789L, result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(beforeIndex + 1, getPathIndex());
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedNumber() throws Exception {
    String numberString = "9876543210";
    char[] buffer = numberString.toCharArray();
    setPeeked(JsonReader.PEEKED_NUMBER);
    setPeekedNumberLength(buffer.length);
    setPos(0);
    setBuffer(new char[JsonReader.BUFFER_SIZE]);
    System.arraycopy(buffer, 0, jsonReader.buffer, 0, buffer.length);
    int beforeIndex = getPathIndex();

    long result = jsonReader.nextLong();

    assertEquals(Long.parseLong(numberString), result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(beforeIndex + 1, getPathIndex());
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedSingleQuoted_validLong() throws Exception {
    String longString = "12345";
    setPeeked(JsonReader.PEEKED_SINGLE_QUOTED);
    // Stub nextQuotedValue to return the longString
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(longString).when(spyReader).nextQuotedValue('\'');

    // Replace jsonReader with spyReader for this test
    jsonReader = spyReader;

    int beforeIndex = getPathIndex();

    long result = jsonReader.nextLong();

    assertEquals(Long.parseLong(longString), result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(beforeIndex + 1, getPathIndex());
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedDoubleQuoted_validLong() throws Exception {
    String longString = "54321";
    setPeeked(JsonReader.PEEKED_DOUBLE_QUOTED);
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(longString).when(spyReader).nextQuotedValue('"');
    jsonReader = spyReader;

    int beforeIndex = getPathIndex();

    long result = jsonReader.nextLong();

    assertEquals(Long.parseLong(longString), result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(beforeIndex + 1, getPathIndex());
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedUnquoted_validLong() throws Exception {
    String longString = "67890";
    setPeeked(JsonReader.PEEKED_UNQUOTED);
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(longString).when(spyReader).nextUnquotedValue();
    jsonReader = spyReader;

    int beforeIndex = getPathIndex();

    long result = jsonReader.nextLong();

    assertEquals(Long.parseLong(longString), result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(beforeIndex + 1, getPathIndex());
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedUnquoted_invalidLongFallsBackToDouble() throws Exception {
    String doubleString = "12345.0";
    setPeeked(JsonReader.PEEKED_UNQUOTED);
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(doubleString).when(spyReader).nextUnquotedValue();
    jsonReader = spyReader;

    int beforeIndex = getPathIndex();

    long result = jsonReader.nextLong();

    assertEquals((long) Double.parseDouble(doubleString), result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(beforeIndex + 1, getPathIndex());
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedSingleQuoted_invalidLongFallsBackToDouble() throws Exception {
    String doubleString = "12345.0";
    setPeeked(JsonReader.PEEKED_SINGLE_QUOTED);
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(doubleString).when(spyReader).nextQuotedValue('\'');
    jsonReader = spyReader;

    int beforeIndex = getPathIndex();

    long result = jsonReader.nextLong();

    assertEquals((long) Double.parseDouble(doubleString), result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(beforeIndex + 1, getPathIndex());
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedDoubleQuoted_invalidLongFallsBackToDouble() throws Exception {
    String doubleString = "12345.0";
    setPeeked(JsonReader.PEEKED_DOUBLE_QUOTED);
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(doubleString).when(spyReader).nextQuotedValue('"');
    jsonReader = spyReader;

    int beforeIndex = getPathIndex();

    long result = jsonReader.nextLong();

    assertEquals((long) Double.parseDouble(doubleString), result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(beforeIndex + 1, getPathIndex());
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedNumberInvalidDoublePrecisionLoss() throws Exception {
    String doubleString = "12345.67";
    setPeeked(JsonReader.PEEKED_NUMBER);
    setPeekedNumberLength(doubleString.length());
    setPos(0);
    setBuffer(new char[JsonReader.BUFFER_SIZE]);
    System.arraycopy(doubleString.toCharArray(), 0, jsonReader.buffer, 0, doubleString.length());

    // Override peekedString to doubleString to simulate parse fallback
    try {
      Field peekedStringField = JsonReader.class.getDeclaredField("peekedString");
      peekedStringField.setAccessible(true);
      peekedStringField.set(jsonReader, doubleString);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // Set peeked to PEEKED_NUMBER to force fallback parsing
    setPeeked(JsonReader.PEEKED_NUMBER);

    // The cast from double to long will lose precision, expect NumberFormatException
    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> jsonReader.nextLong());
    assertTrue(ex.getMessage().contains("Expected a long but was"));
  }

  @Test
    @Timeout(8000)
  void testNextLong_peekedInvalid_throwsIllegalStateException() throws Exception {
    setPeeked(JsonReader.PEEKED_NONE);

    // Spy doPeek to return invalid peeked value
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(999).when(spyReader).doPeek();
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    doReturn(" at path $").when(spyReader).locationString();
    jsonReader = spyReader;

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonReader.nextLong());
    assertTrue(ex.getMessage().contains("Expected a long but was"));
  }
}