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

class JsonReaderPeekNumberTest {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    // Mock Reader since JsonReader requires a Reader but peekNumber does not consume it directly here
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokePeekNumber() throws Exception {
    Method peekNumberMethod = JsonReader.class.getDeclaredMethod("peekNumber");
    peekNumberMethod.setAccessible(true);
    return (int) peekNumberMethod.invoke(jsonReader);
  }

  private void setBufferAndPosLimit(String content) throws Exception {
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    int len = Math.min(content.length(), buffer.length);
    content.getChars(0, len, buffer, 0);

    // Set private fields buffer, pos, limit
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, buffer);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, 0);

    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, len);
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

  @Test
    @Timeout(8000)
  void testPeekNumber_simplePositiveInteger() throws Exception {
    setBufferAndPosLimit("12345 ");
    int result = invokePeekNumber();
    assertEquals(15, result); // PEEKED_LONG
    assertEquals(12345L, getPeekedLong());
    assertEquals(5, (int) getPeekedNumberLength()); // length 5
    assertEquals(15, getPeeked());
    assertEquals(5, getPos());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_simpleNegativeInteger() throws Exception {
    setBufferAndPosLimit("-9876 ");
    int result = invokePeekNumber();
    assertEquals(15, result); // PEEKED_LONG
    assertEquals(-9876L, getPeekedLong());
    assertEquals(5, getPeekedNumberLength()); // length 5
    assertEquals(15, getPeeked());
    assertEquals(5, getPos());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_leadingZeroReturnsNone() throws Exception {
    setBufferAndPosLimit("0123 ");
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_decimalNumber() throws Exception {
    setBufferAndPosLimit("123.456 ");
    int result = invokePeekNumber();
    assertEquals(16, result); // PEEKED_NUMBER
    assertEquals(7, getPeekedNumberLength()); // length of "123.456"
    assertEquals(16, getPeeked());
    // pos should not be advanced for PEEKED_NUMBER
    assertEquals(0, getPos());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_exponentNumber() throws Exception {
    setBufferAndPosLimit("1e10 ");
    int result = invokePeekNumber();
    assertEquals(16, result); // PEEKED_NUMBER
    assertEquals(4, getPeekedNumberLength()); // length of "1e10"
    assertEquals(16, getPeeked());
    assertEquals(0, getPos());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_exponentWithSign() throws Exception {
    setBufferAndPosLimit("2E-3 ");
    int result = invokePeekNumber();
    assertEquals(16, result); // PEEKED_NUMBER
    assertEquals(4, getPeekedNumberLength()); // length of "2E-3"
    assertEquals(16, getPeeked());
    assertEquals(0, getPos());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_invalidPlusSign() throws Exception {
    setBufferAndPosLimit("+123 ");
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_invalidPlusSignAfterDigit() throws Exception {
    setBufferAndPosLimit("1+23 ");
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_invalidMinusSignInMiddle() throws Exception {
    setBufferAndPosLimit("12-3 ");
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_emptyBufferReturnsNone() throws Exception {
    setBufferAndPosLimit("");
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_longNumberTooLongReturnsNone() throws Exception {
    // Fill buffer with '1' repeated buffer length times (1024)
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < JsonReader.BUFFER_SIZE; i++) {
      sb.append('1');
    }
    setBufferAndPosLimit(sb.toString());
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE because it's too long
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_numberWithNonLiteralTrailingChar() throws Exception {
    setBufferAndPosLimit("1234,");
    int result = invokePeekNumber();
    assertEquals(15, result); // PEEKED_LONG
    assertEquals(1234L, getPeekedLong());
    assertEquals(4, getPeekedNumberLength());
    assertEquals(15, getPeeked());
    assertEquals(4, getPos());
  }

  @Test
    @Timeout(8000)
  void testPeekNumber_numberWithLiteralTrailingCharReturnsNone() throws Exception {
    setBufferAndPosLimit("1234a");
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE because 'a' is literal (isLiteral returns true)
  }

  private int getPos() throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    return posField.getInt(jsonReader);
  }

}