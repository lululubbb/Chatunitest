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

public class JsonReader_196_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokePeekKeyword() throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("peekKeyword");
    method.setAccessible(true);
    return (int) method.invoke(jsonReader);
  }

  private void setBufferAndPos(String content, int pos, int limit) throws Exception {
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    char[] contentChars = content.toCharArray();
    System.arraycopy(contentChars, 0, buffer, 0, contentChars.length);

    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);

    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void setPeeked(int value) throws Exception {
    Field peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  private void setPos(int pos) throws Exception {
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  private void setLimit(int limit) throws Exception {
    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void mockFillBuffer(boolean returnValue) throws Exception {
    // Spy on jsonReader to mock fillBuffer
    JsonReader spyReader = spy(jsonReader);
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    doReturn(returnValue).when(spyReader).fillBuffer(anyInt());

    // Replace original jsonReader with spyReader
    jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_trueLowerCase() throws Exception {
    setBufferAndPos("true ", 0, 5);
    setPeeked(0);
    int result = invokePeekKeyword();
    assertEquals(5, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_trueUpperCase() throws Exception {
    setBufferAndPos("TRUE ", 0, 5);
    setPeeked(0);
    int result = invokePeekKeyword();
    assertEquals(5, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_falseLowerCase() throws Exception {
    setBufferAndPos("false ", 0, 6);
    setPeeked(0);
    int result = invokePeekKeyword();
    assertEquals(6, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(5, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_falseUpperCase() throws Exception {
    setBufferAndPos("FALSE ", 0, 6);
    setPeeked(0);
    int result = invokePeekKeyword();
    assertEquals(6, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(5, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_nullLowerCase() throws Exception {
    setBufferAndPos("null ", 0, 5);
    setPeeked(0);
    int result = invokePeekKeyword();
    assertEquals(7, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_nullUpperCase() throws Exception {
    setBufferAndPos("NULL ", 0, 5);
    setPeeked(0);
    int result = invokePeekKeyword();
    assertEquals(7, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_invalidFirstChar() throws Exception {
    setBufferAndPos("x", 0, 1);
    setPeeked(0);
    int result = invokePeekKeyword();
    assertEquals(0, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(0, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_partialBuffer_fillBufferFalse() throws Exception {
    setBufferAndPos("tru", 0, 3);
    setPeeked(0);

    // Spy and mock fillBuffer to return false
    JsonReader spyReader = spy(jsonReader);
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    doReturn(false).when(spyReader).fillBuffer(anyInt());

    // Replace jsonReader with spyReader for this test
    jsonReader = spyReader;

    int result = invokePeekKeyword();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_nonMatchingCharInKeyword() throws Exception {
    setBufferAndPos("tRueX", 0, 5);
    setPeeked(0);

    // The last char 'X' after "true" is a literal, so peekKeyword returns PEEKED_NONE
    int result = invokePeekKeyword();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_literalAfterKeyword() throws Exception {
    // Setup buffer with "trueX" where X is a literal char (like 'a')
    setBufferAndPos("truea", 0, 5);
    setPeeked(0);

    // Spy and mock fillBuffer to always true to check next char
    JsonReader spyReader = spy(jsonReader);
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    doReturn(true).when(spyReader).fillBuffer(anyInt());

    // Replace jsonReader with spyReader for this test
    jsonReader = spyReader;

    int result = invokePeekKeyword();
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_fillBufferCalledWhenNeeded() throws Exception {
    // Setup buffer with "tru" and pos=0, limit=3
    setBufferAndPos("tru", 0, 3);
    setPeeked(0);

    // Spy and mock fillBuffer to return true once, then false
    JsonReader spyReader = spy(jsonReader);
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    when(spyReader.fillBuffer(4)).thenReturn(true);
    when(spyReader.fillBuffer(5)).thenReturn(false);

    // Replace jsonReader with spyReader for this test
    jsonReader = spyReader;

    // Extend buffer to have 'e' at position 3 after fillBuffer call (simulate)
    Field bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    buffer[3] = 'e';

    // Increase limit to 4 to simulate fillBuffer success
    setLimit(4);

    int result = invokePeekKeyword();
    assertEquals(5, result);
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }
}