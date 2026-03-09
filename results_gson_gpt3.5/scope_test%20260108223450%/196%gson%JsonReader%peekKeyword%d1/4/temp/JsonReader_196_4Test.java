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

public class JsonReaderPeekKeywordTest {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokePeekKeyword() throws Exception {
    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    return (int) peekKeyword.invoke(jsonReader);
  }

  private void setBuffer(char[] content, int length) throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    System.arraycopy(content, 0, buffer, 0, length);

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

  private void setPeeked(int peeked) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, peeked);
  }

  private void setLimit(int limit) throws Exception {
    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void mockFillBuffer(boolean returnValue, int calls) throws Exception {
    // fillBuffer is private, so use spy to override it
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader, "fillBuffer", anyInt());
    jsonReader = spyReader;
  }

  private void setFillBufferReturn(boolean returnValue) throws Exception {
    JsonReader spyReader = spy(jsonReader);
    doAnswer(invocation -> returnValue).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_true_lowercase() throws Exception {
    char[] content = "true ".toCharArray();
    setBuffer(content, content.length);
    int result = invokePeekKeyword();
    assertEquals(5, result); // PEEKED_TRUE = 5
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_true_uppercase() throws Exception {
    char[] content = "TRUE ".toCharArray();
    setBuffer(content, content.length);
    int result = invokePeekKeyword();
    assertEquals(5, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_false_lowercase() throws Exception {
    char[] content = "false ".toCharArray();
    setBuffer(content, content.length);
    int result = invokePeekKeyword();
    assertEquals(6, result); // PEEKED_FALSE = 6
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(5, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_false_uppercase() throws Exception {
    char[] content = "FALSE ".toCharArray();
    setBuffer(content, content.length);
    int result = invokePeekKeyword();
    assertEquals(6, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(5, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_null_lowercase() throws Exception {
    char[] content = "null ".toCharArray();
    setBuffer(content, content.length);
    int result = invokePeekKeyword();
    assertEquals(7, result); // PEEKED_NULL = 7
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_null_uppercase() throws Exception {
    char[] content = "NULL ".toCharArray();
    setBuffer(content, content.length);
    int result = invokePeekKeyword();
    assertEquals(7, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_nonKeywordChar() throws Exception {
    char[] content = "x".toCharArray();
    setBuffer(content, content.length);
    int result = invokePeekKeyword();
    assertEquals(0, result); // PEEKED_NONE = 0
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_partialMatchInsufficientBuffer() throws Exception {
    char[] content = "tru".toCharArray(); // incomplete "true"
    setBuffer(content, content.length);

    // Spy and mock fillBuffer to return false, simulating EOF
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;

    int result = invokePeekKeyword();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_partialMatchFillBufferTrue() throws Exception {
    char[] content = "tru".toCharArray(); // incomplete "true"
    setBuffer(content, content.length);

    // Spy and mock fillBuffer to return true, simulating buffer fill success
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(anyInt());
    // Also set limit to at least length of keyword
    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, 10);
    jsonReader = spyReader;

    // We need to fill buffer with proper chars after pos+3, so that match fails or passes
    // But since buffer is private, modify buffer directly
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    buffer[3] = 'e'; // complete "true"
    buffer[4] = ' '; // non-literal char after keyword

    int result = invokePeekKeyword();
    assertEquals(5, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_followedByLiteralChar() throws Exception {
    char[] content = "truea".toCharArray(); // "true" followed by 'a' which is a literal char
    setBuffer(content, content.length);

    // Spy and mock fillBuffer to true so it attempts to read pos+length char
    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;

    // The 'a' is literal, so peekKeyword should return PEEKED_NONE
    int result = invokePeekKeyword();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_fillBufferNeededForLiteralCheckReturnsFalse() throws Exception {
    char[] content = "true".toCharArray(); // exactly "true" with no extra char
    setBuffer(content, content.length);

    // Spy and mock fillBuffer to false when called for pos+length+1 check
    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;

    // It should accept keyword because pos+length == limit and fillBuffer returns false,
    // so no literal char after keyword
    int result = invokePeekKeyword();
    assertEquals(5, result);
  }
}