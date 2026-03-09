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

class JsonReaderPeekKeywordTest {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokePeekKeyword() throws Exception {
    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    return (int) peekKeyword.invoke(jsonReader);
  }

  private void setBufferAndPos(char[] content, int pos, int limit) throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    System.arraycopy(content, 0, buffer, 0, content.length);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);

    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void setPeekedField(int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private void setLenient(boolean lenient) throws Exception {
    Method setLenient = JsonReader.class.getDeclaredMethod("setLenient", boolean.class);
    setLenient.invoke(jsonReader, lenient);
  }

  private boolean invokeIsLiteral(char c) throws Exception {
    Method isLiteral = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteral.setAccessible(true);
    return (boolean) isLiteral.invoke(jsonReader, c);
  }

  private boolean fillBufferStub(int minimum) throws Exception {
    Method fillBuffer = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBuffer.setAccessible(true);
    return (boolean) fillBuffer.invoke(jsonReader, minimum);
  }

  // To stub fillBuffer method, override it using a spy
  private JsonReader spyJsonReader() {
    return Mockito.spy(jsonReader);
  }

  @Test
    @Timeout(8000)
  void peekKeyword_returnsPEEKED_TRUE_forTrueKeyword() throws Exception {
    // Arrange: buffer contains "true" at pos 0
    char[] content = "true".toCharArray();
    setBufferAndPos(content, 0, content.length);

    JsonReader spy = spyJsonReader();

    // Stub fillBuffer to always return false (no more data needed)
    doReturn(false).when(spy).fillBuffer(anyInt());

    // Stub isLiteral to return false for next char after keyword (simulate end or non-literal)
    doReturn(false).when(spy).isLiteral(anyChar());

    // Act
    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    int result = (int) peekKeyword.invoke(spy);

    // Assert
    Field PEEKED_TRUE = JsonReader.class.getDeclaredField("PEEKED_TRUE");
    PEEKED_TRUE.setAccessible(true);
    int expected = (int) PEEKED_TRUE.get(null);

    assertEquals(expected, result);

    // pos advanced by length of "true"
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(spy));
  }

  @Test
    @Timeout(8000)
  void peekKeyword_returnsPEEKED_FALSE_forFalseKeyword() throws Exception {
    char[] content = "false".toCharArray();
    setBufferAndPos(content, 0, content.length);

    JsonReader spy = spyJsonReader();
    doReturn(false).when(spy).fillBuffer(anyInt());
    doReturn(false).when(spy).isLiteral(anyChar());

    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    int result = (int) peekKeyword.invoke(spy);

    Field PEEKED_FALSE = JsonReader.class.getDeclaredField("PEEKED_FALSE");
    PEEKED_FALSE.setAccessible(true);
    int expected = (int) PEEKED_FALSE.get(null);

    assertEquals(expected, result);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(5, posField.getInt(spy));
  }

  @Test
    @Timeout(8000)
  void peekKeyword_returnsPEEKED_NULL_forNullKeyword() throws Exception {
    char[] content = "null".toCharArray();
    setBufferAndPos(content, 0, content.length);

    JsonReader spy = spyJsonReader();
    doReturn(false).when(spy).fillBuffer(anyInt());
    doReturn(false).when(spy).isLiteral(anyChar());

    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    int result = (int) peekKeyword.invoke(spy);

    Field PEEKED_NULL = JsonReader.class.getDeclaredField("PEEKED_NULL");
    PEEKED_NULL.setAccessible(true);
    int expected = (int) PEEKED_NULL.get(null);

    assertEquals(expected, result);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(spy));
  }

  @Test
    @Timeout(8000)
  void peekKeyword_returnsPEEKED_NONE_forNonKeywordStartChar() throws Exception {
    char[] content = "x".toCharArray();
    setBufferAndPos(content, 0, content.length);

    int result = invokePeekKeyword();

    Field PEEKED_NONE = JsonReader.class.getDeclaredField("PEEKED_NONE");
    PEEKED_NONE.setAccessible(true);
    int expected = (int) PEEKED_NONE.get(null);

    assertEquals(expected, result);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(0, posField.getInt(jsonReader)); // pos unchanged
  }

  @Test
    @Timeout(8000)
  void peekKeyword_returnsPEEKED_NONE_ifBufferEndsBeforeKeywordComplete() throws Exception {
    // buffer contains "tru" only, pos=0, limit=3
    char[] content = "tru".toCharArray();
    setBufferAndPos(content, 0, content.length);

    JsonReader spy = spyJsonReader();

    // fillBuffer returns false: cannot fill more
    doReturn(false).when(spy).fillBuffer(anyInt());

    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    int result = (int) peekKeyword.invoke(spy);

    Field PEEKED_NONE = JsonReader.class.getDeclaredField("PEEKED_NONE");
    PEEKED_NONE.setAccessible(true);
    int expected = (int) PEEKED_NONE.get(null);

    assertEquals(expected, result);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(0, posField.getInt(spy)); // pos unchanged
  }

  @Test
    @Timeout(8000)
  void peekKeyword_returnsPEEKED_NONE_ifKeywordCharsMismatch() throws Exception {
    // buffer contains "trve" (mismatch 'v' instead of 'u')
    char[] content = "trve".toCharArray();
    setBufferAndPos(content, 0, content.length);

    JsonReader spy = spyJsonReader();

    doReturn(false).when(spy).fillBuffer(anyInt());

    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    int result = (int) peekKeyword.invoke(spy);

    Field PEEKED_NONE = JsonReader.class.getDeclaredField("PEEKED_NONE");
    PEEKED_NONE.setAccessible(true);
    int expected = (int) PEEKED_NONE.get(null);

    assertEquals(expected, result);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(0, posField.getInt(spy)); // pos unchanged
  }

  @Test
    @Timeout(8000)
  void peekKeyword_returnsPEEKED_NONE_ifNextCharIsLiteral() throws Exception {
    // buffer contains "trueX" where X is a literal char
    char[] content = "trueX".toCharArray();
    setBufferAndPos(content, 0, content.length);

    JsonReader spy = spyJsonReader();

    // fillBuffer returns false (no more data needed)
    doReturn(false).when(spy).fillBuffer(anyInt());

    // isLiteral returns true for 'X' to simulate literal after keyword
    doAnswer(invocation -> {
      char c = invocation.getArgument(0);
      return c == 'X';
    }).when(spy).isLiteral(anyChar());

    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    int result = (int) peekKeyword.invoke(spy);

    Field PEEKED_NONE = JsonReader.class.getDeclaredField("PEEKED_NONE");
    PEEKED_NONE.setAccessible(true);
    int expected = (int) PEEKED_NONE.get(null);

    assertEquals(expected, result);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(0, posField.getInt(spy)); // pos unchanged
  }

  @Test
    @Timeout(8000)
  void peekKeyword_callsFillBufferWhenNeeded() throws Exception {
    // buffer contains "tru" only, pos=0, limit=3
    char[] content = "tru".toCharArray();
    setBufferAndPos(content, 0, content.length);

    JsonReader spy = spyJsonReader();

    // fillBuffer returns true to simulate buffer fill success
    doReturn(true).when(spy).fillBuffer(anyInt());

    // isLiteral returns false for next char after keyword
    doReturn(false).when(spy).isLiteral(anyChar());

    // Manually append 'e' at pos+3 after fillBuffer call to simulate buffer fill
    doAnswer(invocation -> {
      Field bufferField = JsonReader.class.getDeclaredField("buffer");
      bufferField.setAccessible(true);
      char[] buffer = (char[]) bufferField.get(spy);
      buffer[3] = 'e';
      return true;
    }).when(spy).fillBuffer(4);

    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    int result = (int) peekKeyword.invoke(spy);

    Field PEEKED_TRUE = JsonReader.class.getDeclaredField("PEEKED_TRUE");
    PEEKED_TRUE.setAccessible(true);
    int expected = (int) PEEKED_TRUE.get(null);

    assertEquals(expected, result);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(spy));
  }
}