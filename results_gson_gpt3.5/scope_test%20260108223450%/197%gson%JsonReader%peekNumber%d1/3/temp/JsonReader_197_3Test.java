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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReaderPeekNumberTest {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokePeekNumber() throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("peekNumber");
    method.setAccessible(true);
    try {
      return (int) method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      // Unwrap IOException from InvocationTargetException
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  private void setBuffer(String s) throws Exception {
    // Fill buffer with chars from s and set pos and limit accordingly
    char[] chars = new char[1024];
    int len = s.length();
    s.getChars(0, len, chars, 0);

    // Use reflection to set private fields buffer, pos, limit
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, chars);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, len);
  }

  private void setLenient(boolean lenient) throws Exception {
    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, lenient);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_simplePositiveLong() throws Exception {
    setBuffer("12345 ");
    int result = invokePeekNumber();
    assertEquals(15, result); // PEEKED_LONG = 15

    var peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
    peekedLongField.setAccessible(true);
    long value = peekedLongField.getLong(jsonReader);
    assertEquals(12345L, value);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = posField.getInt(jsonReader);
    assertEquals(5, pos);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_simpleNegativeLong() throws Exception {
    setBuffer("-9876 ");
    int result = invokePeekNumber();
    assertEquals(15, result); // PEEKED_LONG = 15

    var peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
    peekedLongField.setAccessible(true);
    long value = peekedLongField.getLong(jsonReader);
    assertEquals(-9876L, value);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = posField.getInt(jsonReader);
    assertEquals(5, pos);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_zeroNegative() throws Exception {
    setBuffer("-0 ");
    int result = invokePeekNumber();
    // According to code, -0 is not stored as long but as PEEKED_NUMBER
    assertEquals(16, result); // PEEKED_NUMBER = 16

    var peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
    peekedNumberLengthField.setAccessible(true);
    int length = peekedNumberLengthField.getInt(jsonReader);
    assertEquals(2, length);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_zeroPositive() throws Exception {
    setBuffer("0 ");
    int result = invokePeekNumber();
    assertEquals(15, result); // PEEKED_LONG = 15

    var peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
    peekedLongField.setAccessible(true);
    long value = peekedLongField.getLong(jsonReader);
    assertEquals(0L, value);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_leadingZeroInvalid() throws Exception {
    setBuffer("01 ");
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE = 0
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_decimalNumber() throws Exception {
    setBuffer("123.456 ");
    int result = invokePeekNumber();
    assertEquals(16, result); // PEEKED_NUMBER = 16

    var peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
    peekedNumberLengthField.setAccessible(true);
    int length = peekedNumberLengthField.getInt(jsonReader);
    assertEquals(7, length);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_exponentNumber() throws Exception {
    setBuffer("1e10 ");
    int result = invokePeekNumber();
    assertEquals(16, result); // PEEKED_NUMBER = 16

    var peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
    peekedNumberLengthField.setAccessible(true);
    int length = peekedNumberLengthField.getInt(jsonReader);
    assertEquals(4, length);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_exponentWithSign() throws Exception {
    setBuffer("1e-10 ");
    int result = invokePeekNumber();
    assertEquals(16, result); // PEEKED_NUMBER = 16

    var peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
    peekedNumberLengthField.setAccessible(true);
    int length = peekedNumberLengthField.getInt(jsonReader);
    assertEquals(5, length);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_invalidSignPosition() throws Exception {
    setBuffer("1-2 ");
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE = 0
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_bufferTooLong() throws Exception {
    // create a string longer than buffer length (1024)
    char[] chars = new char[1025];
    for (int i = 0; i < chars.length; i++) {
      chars[i] = '1';
    }
    String longNumber = new String(chars);
    setBuffer(longNumber);

    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE = 0 because too long
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_fillBufferCalled() throws Exception {
    // Spy on JsonReader to mock fillBuffer behavior
    JsonReader spyReader = spy(new JsonReader(mockReader));
    // Setup buffer with partial number and pos/limit so fillBuffer is triggered
    char[] partialBuffer = new char[1024];
    partialBuffer[0] = '1';
    partialBuffer[1] = '2';
    partialBuffer[2] = '3';
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(spyReader, partialBuffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, 3);

    // When fillBuffer is called with argument > 3, return false to simulate EOF
    doReturn(false).when(spyReader).fillBuffer(anyInt());

    Method peekNumberMethod = JsonReader.class.getDeclaredMethod("peekNumber");
    peekNumberMethod.setAccessible(true);
    int result = (int) peekNumberMethod.invoke(spyReader);

    assertEquals(15, result); // It should parse "123" as long
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_nonDigitLiteralBreaks() throws Exception {
    setBuffer("123x ");
    int result = invokePeekNumber();
    assertEquals(15, result); // PEEKED_LONG = 15 because 'x' is not literal and breaks loop

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    int pos = posField.getInt(jsonReader);
    assertEquals(3, pos); // pos advanced just past digits
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_invalidLiteralReturnsNone() throws Exception {
    setBuffer("123@ ");
    // Override isLiteral to return true for '@' to trigger PEEKED_NONE return
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).isLiteral('@');

    // Set buffer and pos/limit on spy
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(spyReader, jsonReader.buffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, 4);

    Method peekNumberMethod = JsonReader.class.getDeclaredMethod("peekNumber");
    peekNumberMethod.setAccessible(true);
    int result = (int) peekNumberMethod.invoke(spyReader);

    assertEquals(0, result); // PEEKED_NONE = 0
  }
}