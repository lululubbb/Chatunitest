package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_196_3Test {
  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokePeekKeyword() throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("peekKeyword");
    method.setAccessible(true);
    try {
      return (int) method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void setBufferAndLimit(String content) throws Exception {
    // Use reflection to set private fields buffer, pos, limit
    char[] buffer = new char[1024];
    content.getChars(0, content.length(), buffer, 0);
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, buffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, content.length());
  }

  private void setPeeked(int value) throws Exception {
    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private void setPos(int pos) throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  private void setLimit(int limit) throws Exception {
    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void mockFillBuffer(boolean result) throws Exception {
    Method fillBuffer = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBuffer.setAccessible(true);
    // Spy jsonReader to mock fillBuffer method
    JsonReader spyReader = spy(jsonReader);
    doReturn(result).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;
  }

  private void setBufferCharAt(int index, char c) throws Exception {
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    buffer[index] = c;
  }

  private void setLenient(boolean lenient) throws Exception {
    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, lenient);
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_trueLowercase() throws Throwable {
    setBufferAndLimit("true");
    int result = invokePeekKeyword();
    assertEquals(5, result); // PEEKED_TRUE = 5
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_trueUppercase() throws Throwable {
    setBufferAndLimit("TRUE");
    int result = invokePeekKeyword();
    assertEquals(5, result); // PEEKED_TRUE = 5
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_falseLowercase() throws Throwable {
    setBufferAndLimit("false");
    int result = invokePeekKeyword();
    assertEquals(6, result); // PEEKED_FALSE = 6
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_falseUppercase() throws Throwable {
    setBufferAndLimit("FALSE");
    int result = invokePeekKeyword();
    assertEquals(6, result); // PEEKED_FALSE = 6
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_nullLowercase() throws Throwable {
    setBufferAndLimit("null");
    int result = invokePeekKeyword();
    assertEquals(7, result); // PEEKED_NULL = 7
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_nullUppercase() throws Throwable {
    setBufferAndLimit("NULL");
    int result = invokePeekKeyword();
    assertEquals(7, result); // PEEKED_NULL = 7
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_partialMatchReturnsNone() throws Throwable {
    setBufferAndLimit("truX");
    int result = invokePeekKeyword();
    assertEquals(0, result); // PEEKED_NONE = 0
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_nonMatchingFirstCharReturnsNone() throws Throwable {
    setBufferAndLimit("abc");
    int result = invokePeekKeyword();
    assertEquals(0, result); // PEEKED_NONE = 0
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_insufficientBufferAndFillBufferFalseReturnsNone() throws Throwable {
    setBufferAndLimit("tr");
    // pos + i >= limit will be true for i=2, so fillBuffer is called
    // Mock fillBuffer to return false
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    buffer[0] = 't';
    buffer[1] = 'r';
    spyReader.pos = 0;
    spyReader.limit = 2;
    jsonReader = spyReader;

    Method method = JsonReader.class.getDeclaredMethod("peekKeyword");
    method.setAccessible(true);
    int result = (int) method.invoke(jsonReader);
    assertEquals(0, result); // PEEKED_NONE = 0
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_followedByLiteralCharReturnsNone() throws Throwable {
    // "trueX" where X is a literal char
    setBufferAndLimit("trueX");
    // Mock fillBuffer to return true for length+1
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(anyInt());
    // Set buffer[pos + length] to a literal char, e.g. 'a'
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    buffer[4] = 'a'; // 'a' is a literal char

    spyReader.pos = 0;
    spyReader.limit = 5;
    jsonReader = spyReader;

    Method method = JsonReader.class.getDeclaredMethod("peekKeyword");
    method.setAccessible(true);
    int result = (int) method.invoke(jsonReader);
    assertEquals(0, result); // PEEKED_NONE = 0
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_followedByNonLiteralCharReturnsPeeked() throws Throwable {
    // "true " (space after true) should return PEEKED_TRUE
    setBufferAndLimit("true ");
    // Mock fillBuffer to return true for length+1
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(anyInt());
    // Set buffer[pos + length] to a non-literal char, e.g. ' '
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    buffer[4] = ' '; // space is not literal

    spyReader.pos = 0;
    spyReader.limit = 5;
    jsonReader = spyReader;

    Method method = JsonReader.class.getDeclaredMethod("peekKeyword");
    method.setAccessible(true);
    int result = (int) method.invoke(jsonReader);
    assertEquals(5, result); // PEEKED_TRUE = 5
  }
}