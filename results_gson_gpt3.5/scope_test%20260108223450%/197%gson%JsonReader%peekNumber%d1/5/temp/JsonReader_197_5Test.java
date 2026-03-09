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

class JsonReader_197_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokePeekNumber() throws Exception {
    Method peekNumberMethod = JsonReader.class.getDeclaredMethod("peekNumber");
    peekNumberMethod.setAccessible(true);
    return (int) peekNumberMethod.invoke(jsonReader);
  }

  private void setBuffer(char[] chars, int length) throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    System.arraycopy(chars, 0, buffer, 0, length);

    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, length);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, 0);
  }

  private void setPos(int pos) throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  private int getPeeked() throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(jsonReader);
  }

  private long getPeekedLong() throws Exception {
    Field peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
    peekedLongField.setAccessible(true);
    return peekedLongField.getLong(jsonReader);
  }

  private int getPeekedNumberLength() throws Exception {
    Field peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
    peekedNumberLengthField.setAccessible(true);
    return peekedNumberLengthField.getInt(jsonReader);
  }

  private void setPeeked(int val) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, val);
  }

  private void setLimit(int limit) throws Exception {
    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void setPosLimit(int pos, int limit) throws Exception {
    setPos(pos);
    setLimit(limit);
  }

  private void setLenient(boolean lenient) throws Exception {
    Field lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, lenient);
  }

  private void mockFillBuffer(boolean returnValue) throws Exception {
    // Spy to override fillBuffer(boolean)
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(returnValue).when(spyReader).fillBuffer(anyInt());

    // Replace jsonReader with spyReader for reflection calls
    Field inField = JsonReader.class.getDeclaredField("in");
    inField.setAccessible(true);
    inField.set(spyReader, mockReader);

    this.jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_simplePositiveInteger() throws Exception {
    char[] input = "12345 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(15 /* PEEKED_LONG */, result);
    assertEquals(12345L, getPeekedLong());
    assertEquals(input.length - 1, jsonReader.pos);
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_simpleNegativeInteger() throws Exception {
    char[] input = "-6789 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(15 /* PEEKED_LONG */, result);
    assertEquals(-6789L, getPeekedLong());
    assertEquals(input.length - 1, jsonReader.pos);
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_leadingZeroNotAllowed() throws Exception {
    char[] input = "0123 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(0 /* PEEKED_NONE */, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_decimalNumber() throws Exception {
    char[] input = "123.456 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(16 /* PEEKED_NUMBER */, result);
    assertEquals(input.length - 1, getPeekedNumberLength());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_exponentNumber() throws Exception {
    char[] input = "1.23e10 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(16 /* PEEKED_NUMBER */, result);
    assertEquals(input.length - 1, getPeekedNumberLength());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_negativeExponent() throws Exception {
    char[] input = "-1.23e-10 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(16 /* PEEKED_NUMBER */, result);
    assertEquals(input.length - 1, getPeekedNumberLength());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_invalidPlusSign() throws Exception {
    char[] input = "+123 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(0 /* PEEKED_NONE */, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_invalidPlusAfterE() throws Exception {
    char[] input = "1e+10 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(16 /* PEEKED_NUMBER */, result);
    assertEquals(input.length - 1, getPeekedNumberLength());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_invalidMinusAfterNonE() throws Exception {
    char[] input = "1-23 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(0 /* PEEKED_NONE */, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_nonDigitNonLiteralBreaks() throws Exception {
    char[] input = "1234,".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(15 /* PEEKED_LONG */, result);
    assertEquals(1234L, getPeekedLong());
    assertEquals(4, jsonReader.pos);
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_tooLongNumberReturnsNone() throws Exception {
    char[] input = new char[1024];
    Arrays.fill(input, '9');
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(0 /* PEEKED_NONE */, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_fillBufferReturnsFalseBreaksLoop() throws Exception {
    char[] input = "123".toCharArray();
    setBuffer(input, input.length - 1); // limit less than input length to trigger fillBuffer

    // Spy to mock fillBuffer returns false
    JsonReader spyReader = Mockito.spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(spyReader, input);
    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, input.length - 1);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);
    this.jsonReader = spyReader;

    int result = invokePeekNumber();

    // It should parse number until limit, so "12" only, which is valid long
    assertEquals(15 /* PEEKED_LONG */, result);
    assertEquals(12L, getPeekedLong());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_zeroNegativeZero() throws Exception {
    char[] input = "0 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    // Zero without negative sign is PEEKED_LONG 0
    assertEquals(15 /* PEEKED_LONG */, result);
    assertEquals(0L, getPeekedLong());

    // Negative zero "-0"
    input = "-0 ".toCharArray();
    setBuffer(input, input.length);
    result = invokePeekNumber();

    // Negative zero is not stored as long, so PEEKED_NUMBER
    assertEquals(16 /* PEEKED_NUMBER */, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_LongMinValue() throws Exception {
    // Long.MIN_VALUE = -9223372036854775808
    char[] input = "-9223372036854775808 ".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    // Should parse as PEEKED_LONG
    assertEquals(15 /* PEEKED_LONG */, result);
    assertEquals(Long.MIN_VALUE, getPeekedLong());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_invalidCharacterReturnsNone() throws Exception {
    char[] input = "123a".toCharArray();
    setBuffer(input, input.length);

    int result = invokePeekNumber();

    assertEquals(0 /* PEEKED_NONE */, result);
  }
}