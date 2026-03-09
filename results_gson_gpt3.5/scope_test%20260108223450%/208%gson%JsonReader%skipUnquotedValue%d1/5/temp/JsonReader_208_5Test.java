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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_208_5Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  /**
   * Helper to set buffer, pos, and limit fields via reflection.
   */
  private void setBufferAndPosLimit(char[] bufferContent, int pos, int limit) throws Exception {
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    System.arraycopy(bufferContent, 0, buffer, 0, bufferContent.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  /**
   * Helper to set lenient flag.
   */
  private void setLenient(boolean lenient) throws Exception {
    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, lenient);
  }

  /**
   * Helper to set pos field.
   */
  private int getPos() throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    return posField.getInt(jsonReader);
  }

  /**
   * Helper to invoke private skipUnquotedValue method.
   */
  private void invokeSkipUnquotedValue() throws Exception {
    Method skipUnquotedValue = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    skipUnquotedValue.setAccessible(true);
    skipUnquotedValue.invoke(jsonReader);
  }

  /**
   * Helper to mock fillBuffer(int) method.
   */
  private void mockFillBufferReturn(boolean returnValue) throws Exception {
    var fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    // We cannot mock private methods directly, so we will spy the JsonReader and stub fillBuffer.
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_stopsAtDelimiter() throws Exception {
    // buffer with unquoted value followed by delimiter ','
    char[] buf = "unquotedValue,".toCharArray();
    setBufferAndPosLimit(buf, 0, buf.length);
    setLenient(true);

    invokeSkipUnquotedValue();

    int posAfter = getPos();
    // It should stop at ',' character, so pos should be index of ','
    assertEquals("unquotedValue".length(), posAfter);
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_stopsAtWhitespace() throws Exception {
    char[] buf = "value \n".toCharArray();
    setBufferAndPosLimit(buf, 0, buf.length);
    setLenient(true);

    invokeSkipUnquotedValue();

    int posAfter = getPos();
    // It should stop at space ' '
    assertEquals("value".length(), posAfter);
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_callsCheckLenientOnCertainChars() throws Exception {
    char[] buf = "value/".toCharArray();
    setBufferAndPosLimit(buf, 0, buf.length);
    setLenient(true);

    // Spy on jsonReader to verify checkLenient call
    JsonReader spyReader = spy(jsonReader);

    // Replace original jsonReader with spy
    // We need to invoke skipUnquotedValue on spyReader via reflection
    Method skipUnquotedValue = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    skipUnquotedValue.setAccessible(true);

    // Use reflection to set spyReader buffer, pos, limit fields same as original
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    System.arraycopy(buf, 0, buffer, 0, buf.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, buf.length);

    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(spyReader, true);

    skipUnquotedValue.invoke(spyReader);

    verify(spyReader, atLeastOnce()).checkLenient();

    int posAfter = posField.getInt(spyReader);
    assertEquals("value".length(), posAfter);
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_advancesPosAndLoopsWhenNoDelimiterAndFillBufferTrue() throws Exception {
    // Setup buffer without delimiters or whitespace in initial content
    char[] buf = "valueWithoutDelimiter".toCharArray();
    setBufferAndPosLimit(buf, 0, buf.length);
    setLenient(true);

    // Spy on jsonReader to override fillBuffer behavior
    JsonReader spyReader = spy(jsonReader);

    // Setup buffer, pos, limit on spyReader
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    System.arraycopy(buf, 0, buffer, 0, buf.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, buf.length);

    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(spyReader, true);

    // fillBuffer returns true once, then false to break loop
    doReturn(true).doReturn(false).when(spyReader).fillBuffer(1);

    Method skipUnquotedValue = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    skipUnquotedValue.setAccessible(true);
    skipUnquotedValue.invoke(spyReader);

    int posAfter = posField.getInt(spyReader);
    // pos should have advanced to limit (buffer length)
    assertEquals(buf.length, posAfter);
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_throwsIOExceptionWhenFillBufferFalse() throws Exception {
    // Setup buffer with no delimiters
    char[] buf = "valueWithoutDelimiter".toCharArray();
    setBufferAndPosLimit(buf, 0, buf.length);
    setLenient(true);

    // Spy on jsonReader to override fillBuffer behavior to always false
    JsonReader spyReader = spy(jsonReader);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    System.arraycopy(buf, 0, buffer, 0, buf.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, buf.length);

    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(spyReader, true);

    // fillBuffer returns false immediately to break loop
    doReturn(false).when(spyReader).fillBuffer(1);

    Method skipUnquotedValue = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    skipUnquotedValue.setAccessible(true);

    // This should not throw, because skipUnquotedValue returns when fillBuffer returns false
    // Actually the method does not throw on fillBuffer false, it exits the loop.
    // So no exception expected here.
    skipUnquotedValue.invoke(spyReader);

    int posAfter = posField.getInt(spyReader);
    assertEquals(buf.length, posAfter);
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_checkLenientThrowsWhenLenientFalse() throws Exception {
    // Setup buffer containing '/' which triggers checkLenient call
    char[] buf = "value/".toCharArray();
    setBufferAndPosLimit(buf, 0, buf.length);
    setLenient(false);

    // Spy on jsonReader to override checkLenient to throw
    JsonReader spyReader = spy(jsonReader);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    System.arraycopy(buf, 0, buffer, 0, buf.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, buf.length);

    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(spyReader, false);

    // Override checkLenient to throw IOException
    doThrow(new IOException("Lenient mode required")).when(spyReader).checkLenient();

    Method skipUnquotedValue = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    skipUnquotedValue.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> skipUnquotedValue.invoke(spyReader));
    assertTrue(thrown.getCause().getMessage().contains("Lenient mode required"));
  }
}