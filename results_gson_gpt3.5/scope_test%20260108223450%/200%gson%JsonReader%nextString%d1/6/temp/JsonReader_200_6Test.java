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

public class JsonReader_200_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    try {
      // Initialize stackSize to 1 to avoid ArrayIndexOutOfBoundsException in nextString()
      Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonReader, 1);
      // Initialize pathIndices array element at index 0 to 0
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
  public void testNextString_peekedNone_callsDoPeekAndReturnsUnquotedValue() throws Exception {
    // Set peeked to PEEKED_NONE so doPeek() is called
    setPeeked(0); // PEEKED_NONE = 0

    // Mock doPeek() to return PEEKED_UNQUOTED (10)
    setDoPeekReturn(10);

    // Mock nextUnquotedValue() to return a test string
    setNextUnquotedValue("unquotedValue");

    String result = jsonReader.nextString();

    assertEquals("unquotedValue", result);
    assertEquals(0, getPeeked());
    assertEquals(1, getPathIndicesAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextString_peekedUnquoted_returnsNextUnquotedValue() throws Exception {
    setPeeked(10); // PEEKED_UNQUOTED
    setNextUnquotedValue("unquotedValue");

    String result = jsonReader.nextString();

    assertEquals("unquotedValue", result);
    assertEquals(0, getPeeked());
    assertEquals(1, getPathIndicesAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextString_peekedSingleQuoted_returnsNextQuotedValueWithSingleQuote() throws Exception {
    setPeeked(8); // PEEKED_SINGLE_QUOTED
    setNextQuotedValue('\'' , "singleQuotedValue");

    String result = jsonReader.nextString();

    assertEquals("singleQuotedValue", result);
    assertEquals(0, getPeeked());
    assertEquals(1, getPathIndicesAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextString_peekedDoubleQuoted_returnsNextQuotedValueWithDoubleQuote() throws Exception {
    setPeeked(9); // PEEKED_DOUBLE_QUOTED
    setNextQuotedValue('\"' , "doubleQuotedValue");

    String result = jsonReader.nextString();

    assertEquals("doubleQuotedValue", result);
    assertEquals(0, getPeeked());
    assertEquals(1, getPathIndicesAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextString_peekedBuffered_returnsPeekedStringAndClearsIt() throws Exception {
    setPeeked(11); // PEEKED_BUFFERED
    setPeekedString("bufferedString");

    String result = jsonReader.nextString();

    assertEquals("bufferedString", result);
    assertNull(getPeekedString());
    assertEquals(0, getPeeked());
    assertEquals(1, getPathIndicesAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextString_peekedLong_returnsStringValueOfPeekedLong() throws Exception {
    setPeeked(15); // PEEKED_LONG
    setPeekedLong(123456789L);

    String result = jsonReader.nextString();

    assertEquals("123456789", result);
    assertEquals(0, getPeeked());
    assertEquals(1, getPathIndicesAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextString_peekedNumber_returnsStringFromBufferAndAdvancesPos() throws Exception {
    setPeeked(16); // PEEKED_NUMBER
    setPos(5);
    setPeekedNumberLength(4);
    setBuffer("abcd1234efgh".toCharArray()); // buffer with some chars

    String result = jsonReader.nextString();

    assertEquals("1234", result);
    assertEquals(9, getPos()); // pos should be advanced by peekedNumberLength (5+4=9)
    assertEquals(0, getPeeked());
    assertEquals(1, getPathIndicesAtStackTop());
  }

  @Test
    @Timeout(8000)
  public void testNextString_invalidPeeked_throwsIllegalStateException() throws Exception {
    setPeeked(1); // PEEKED_BEGIN_OBJECT, which is invalid for nextString()

    Exception exception = assertThrows(IllegalStateException.class, () -> jsonReader.nextString());

    String message = exception.getMessage();
    assertTrue(message.startsWith("Expected a string but was "));
  }

  // Helper methods to set private fields and mock private methods

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

  private void setPos(int value) throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, value);
  }

  private int getPos() throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    return posField.getInt(jsonReader);
  }

  private void setBuffer(char[] chars) throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    System.arraycopy(chars, 0, buffer, 0, chars.length);
  }

  private void setDoPeekReturn(int returnValue) throws Exception {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    // Replace doPeek with a spy to return the specified value
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).doPeek();
    // Replace jsonReader with spyReader for nextString invocation
    this.jsonReader = spyReader;
  }

  private void setNextUnquotedValue(String returnValue) throws Exception {
    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextUnquotedValue();
    this.jsonReader = spyReader;
  }

  private void setNextQuotedValue(char quote, String returnValue) throws Exception {
    Method nextQuotedValueMethod = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
    nextQuotedValueMethod.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).nextQuotedValue(quote);
    this.jsonReader = spyReader;
  }

  private int getPathIndicesAtStackTop() throws Exception {
    Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonReader);
    return pathIndices[stackSize - 1];
  }
}