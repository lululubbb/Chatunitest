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

public class JsonReader_200_1Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stackSize and pathIndices for nextString
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

  @Test
    @Timeout(8000)
  void nextString_peekedNone_callsDoPeekAndReturnsUnquoted() throws Exception {
    setPeeked(JsonReader.PEEKED_NONE);

    // Mock doPeek() to return PEEKED_UNQUOTED
    setDoPeekReturnValue(JsonReader.PEEKED_UNQUOTED);

    // Mock nextUnquotedValue() to return "unquotedValue"
    setNextUnquotedValue("unquotedValue");

    String result = jsonReader.nextString();

    assertEquals("unquotedValue", result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(1, getPathIndex(0));
  }

  @Test
    @Timeout(8000)
  void nextString_peekedUnquoted_returnsNextUnquotedValue() throws Exception {
    setPeeked(JsonReader.PEEKED_UNQUOTED);
    setNextUnquotedValue("unquotedValue");

    String result = jsonReader.nextString();

    assertEquals("unquotedValue", result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(1, getPathIndex(0));
  }

  @Test
    @Timeout(8000)
  void nextString_peekedSingleQuoted_returnsNextQuotedValue() throws Exception {
    setPeeked(JsonReader.PEEKED_SINGLE_QUOTED);
    setNextQuotedValue("singleQuotedValue");

    String result = jsonReader.nextString();

    assertEquals("singleQuotedValue", result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(1, getPathIndex(0));
  }

  @Test
    @Timeout(8000)
  void nextString_peekedDoubleQuoted_returnsNextQuotedValue() throws Exception {
    setPeeked(JsonReader.PEEKED_DOUBLE_QUOTED);
    setNextQuotedValue("doubleQuotedValue");

    String result = jsonReader.nextString();

    assertEquals("doubleQuotedValue", result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(1, getPathIndex(0));
  }

  @Test
    @Timeout(8000)
  void nextString_peekedBuffered_returnsPeekedStringAndClears() throws Exception {
    setPeeked(JsonReader.PEEKED_BUFFERED);
    setPeekedString("bufferedValue");

    String result = jsonReader.nextString();

    assertEquals("bufferedValue", result);
    assertNull(getPeekedString());
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(1, getPathIndex(0));
  }

  @Test
    @Timeout(8000)
  void nextString_peekedLong_returnsLongToString() throws Exception {
    setPeeked(JsonReader.PEEKED_LONG);
    setPeekedLong(123456789L);

    String result = jsonReader.nextString();

    assertEquals("123456789", result);
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(1, getPathIndex(0));
  }

  @Test
    @Timeout(8000)
  void nextString_peekedNumber_returnsNumberStringAndAdvancesPos() throws Exception {
    setPeeked(JsonReader.PEEKED_NUMBER);
    char[] buffer = getBuffer();
    String numberString = "12345";
    for (int i = 0; i < numberString.length(); i++) {
      buffer[i] = numberString.charAt(i);
    }
    setBuffer(buffer);
    setPos(0);
    setPeekedNumberLength(numberString.length());
    int initialPos = getPos();

    String result = jsonReader.nextString();

    assertEquals(numberString, result);
    assertEquals(initialPos + numberString.length(), getPos());
    assertEquals(JsonReader.PEEKED_NONE, getPeeked());
    assertEquals(1, getPathIndex(0));
  }

  @Test
    @Timeout(8000)
  void nextString_invalidPeeked_throwsIllegalStateException() throws Exception {
    setPeeked(999); // invalid peeked value

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonReader.nextString());
    assertTrue(thrown.getMessage().startsWith("Expected a string but was"));
  }

  // Helper methods to set private fields and invoke private methods

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

  private void setPeekedString(String value) throws Exception {
    Field peekedStringField = JsonReader.class.getDeclaredField("peekedString");
    peekedStringField.setAccessible(true);
    peekedStringField.set(jsonReader, value);
  }

  private String getPeekedString() throws Exception {
    Field peekedStringField = JsonReader.class.getDeclaredField("peekedString");
    peekedStringField.setAccessible(true);
    return (String) peekedStringField.get(jsonReader);
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

  private int getPos() throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    return posField.getInt(jsonReader);
  }

  private void setPos(int value) throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, value);
  }

  private char[] getBuffer() throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    return (char[]) bufferField.get(jsonReader);
  }

  private void setBuffer(char[] buffer) throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, buffer);
  }

  private int getPathIndex(int index) throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    return pathIndices[index];
  }

  private void setDoPeekReturnValue(int returnValue) throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);

    // Use a spy to override doPeek return value
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).doPeek();

    // Replace jsonReader with spyReader via reflection
    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, mockReader);

    jsonReader = spyReader;
  }

  private void setNextUnquotedValue(String returnValue) throws Exception {
    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextUnquotedValue();

    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, mockReader);

    jsonReader = spyReader;
  }

  private void setNextQuotedValue(String returnValue) throws Exception {
    Method nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextQuotedValue(anyChar());

    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, mockReader);

    jsonReader = spyReader;
  }
}