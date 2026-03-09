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

public class JsonReader_197_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Using a Reader that won't be used because we will set buffer and pos directly
    jsonReader = new JsonReader(mock(Reader.class));
  }

  private int invokePeekNumber() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method peekNumber = JsonReader.class.getDeclaredMethod("peekNumber");
    peekNumber.setAccessible(true);
    return (int) peekNumber.invoke(jsonReader);
  }

  private void setBufferAndPos(String input) throws Exception {
    // Set buffer chars from input string, pos=0, limit=input length
    char[] buffer = new char[1024];
    System.arraycopy(input.toCharArray(), 0, buffer, 0, input.length());
    java.lang.reflect.Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, buffer);

    java.lang.reflect.Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, 0);

    java.lang.reflect.Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, input.length());
  }

  private void setPos(int pos) throws Exception {
    java.lang.reflect.Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  private void setLimit(int limit) throws Exception {
    java.lang.reflect.Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void setPeeked(int value) throws Exception {
    java.lang.reflect.Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private int getPeeked() throws Exception {
    java.lang.reflect.Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    return peekedField.getInt(jsonReader);
  }

  private long getPeekedLong() throws Exception {
    java.lang.reflect.Field peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
    peekedLongField.setAccessible(true);
    return peekedLongField.getLong(jsonReader);
  }

  private int getPeekedNumberLength() throws Exception {
    java.lang.reflect.Field peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
    peekedNumberLengthField.setAccessible(true);
    return peekedNumberLengthField.getInt(jsonReader);
  }

  private void setLenient(boolean lenient) throws Exception {
    java.lang.reflect.Field lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, lenient);
  }

  // Mock fillBuffer to always return false (simulate EOF)
  private void mockFillBufferFalse() throws Exception {
    JsonReader spy = spy(jsonReader);
    doReturn(false).when(spy).fillBuffer(anyInt());
    jsonReader = spy;
  }

  // Mock fillBuffer to return true once, then false (simulate partial buffer refill)
  private void mockFillBufferTrueThenFalse() throws Exception {
    JsonReader spy = spy(jsonReader);
    doReturn(true).doReturn(false).when(spy).fillBuffer(anyInt());
    jsonReader = spy;
  }

  // Mock fillBuffer to return true indefinitely
  private void mockFillBufferTrue() throws Exception {
    JsonReader spy = spy(jsonReader);
    doReturn(true).when(spy).fillBuffer(anyInt());
    jsonReader = spy;
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_simpleInteger() throws Throwable {
    setBufferAndPos("12345");
    setLenient(false);
    int result = invokePeekNumber();
    assertEquals(15, result); // PEEKED_LONG
    assertEquals(12345L, getPeekedLong());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_negativeInteger() throws Throwable {
    setBufferAndPos("-9876");
    int result = invokePeekNumber();
    assertEquals(15, result); // PEEKED_LONG
    assertEquals(-9876L, getPeekedLong());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_zero() throws Throwable {
    setBufferAndPos("0");
    int result = invokePeekNumber();
    // zero is allowed, negative zero is special case
    assertEquals(15, result);
    assertEquals(0L, getPeekedLong());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_zeroLeading_invalid() throws Throwable {
    setBufferAndPos("0123");
    int result = invokePeekNumber();
    assertEquals(0, result); // PEEKED_NONE due to leading zero
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_decimalNumber() throws Throwable {
    setBufferAndPos("123.456");
    int result = invokePeekNumber();
    assertEquals(16, result); // PEEKED_NUMBER (not long)
    assertEquals(7, getPeekedNumberLength());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_exponentNumber() throws Throwable {
    setBufferAndPos("1.23e10");
    int result = invokePeekNumber();
    assertEquals(16, result);
    assertEquals(7, getPeekedNumberLength());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_exponentWithSign() throws Throwable {
    setBufferAndPos("1.23e-10");
    int result = invokePeekNumber();
    assertEquals(16, result);
    assertEquals(8, getPeekedNumberLength());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_invalidSignPosition() throws Throwable {
    setBufferAndPos("1-23");
    int result = invokePeekNumber();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_invalidPlusSign() throws Throwable {
    setBufferAndPos("+123");
    int result = invokePeekNumber();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_emptyBuffer() throws Throwable {
    setBufferAndPos("");
    mockFillBufferFalse();
    int result = invokePeekNumber();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_bufferEndsMidNumber() throws Throwable {
    // "12345" but limit is less than length
    setBufferAndPos("12345");
    setLimit(3);
    mockFillBufferTrueThenFalse();
    int result = invokePeekNumber();
    assertEquals(15, result);
    assertEquals(123L, getPeekedLong());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_tooLongNumber() throws Throwable {
    // Create a number longer than buffer length to trigger PEEKED_NONE
    char[] chars = new char[1024];
    for (int i = 0; i < chars.length; i++) {
      chars[i] = '1';
    }
    java.lang.reflect.Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, chars);

    setPos(0);
    setLimit(chars.length);

    mockFillBufferTrue();

    int result = invokePeekNumber();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_negativeZero() throws Throwable {
    setBufferAndPos("-0");
    int result = invokePeekNumber();
    // -0 is not stored as long, so expect PEEKED_NUMBER
    assertEquals(16, result);
    assertEquals(2, getPeekedNumberLength());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_LongMinValue() throws Throwable {
    // Long.MIN_VALUE is -9223372036854775808
    String longMinStr = "-9223372036854775808";
    setBufferAndPos(longMinStr);
    int result = invokePeekNumber();
    // Should be PEEKED_NUMBER because of Long.MIN_VALUE special case
    assertEquals(16, result);
    assertEquals(longMinStr.length(), getPeekedNumberLength());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_nonDigitLiteral() throws Throwable {
    setBufferAndPos("123a");
    int result = invokePeekNumber();
    assertEquals(15, result);
    assertEquals(123L, getPeekedLong());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_nonLiteralCharacter() throws Throwable {
    setBufferAndPos("123#");
    int result = invokePeekNumber();
    assertEquals(15, result);
    assertEquals(123L, getPeekedLong());
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_decimalWithoutLeadingDigit() throws Throwable {
    setBufferAndPos(".123");
    int result = invokePeekNumber();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_exponentWithoutDigit() throws Throwable {
    setBufferAndPos("1e");
    int result = invokePeekNumber();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekNumber_exponentSignWithoutDigit() throws Throwable {
    setBufferAndPos("1e+");
    int result = invokePeekNumber();
    assertEquals(0, result);
  }
}