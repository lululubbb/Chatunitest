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

public class JsonReader_196_1Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a Reader mock to pass to JsonReader constructor
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokePeekKeyword() throws Exception {
    Method peekKeyword = JsonReader.class.getDeclaredMethod("peekKeyword");
    peekKeyword.setAccessible(true);
    try {
      return (int) peekKeyword.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      // unwrap IOException
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  private void setBufferAndLimit(String content) throws Exception {
    // Use reflection to set private fields buffer, pos, limit
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    int length = content.length();
    content.getChars(0, length, buffer, 0);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    bufferField.set(jsonReader, buffer);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, length);

    // Reset peeked field to PEEKED_NONE
    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, 0);
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

  private void setPeeked(int peeked) throws Exception {
    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, peeked);
  }

  private void setFillBufferBehavior(boolean returnValue) throws Exception {
    // Mock fillBuffer method via spy and doReturn/doCallRealMethod
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;
  }

  private void setFillBufferForPartialMatch() throws Exception {
    // Spy and customize fillBuffer to simulate partial buffer refill
    JsonReader spyReader = spy(jsonReader);
    doAnswer(invocation -> {
      int minimum = invocation.getArgument(0);
      // Simulate filling buffer by increasing limit by minimum (up to buffer size)
      var limitField = JsonReader.class.getDeclaredField("limit");
      limitField.setAccessible(true);
      int currentLimit = limitField.getInt(spyReader);
      int newLimit = Math.min(currentLimit + minimum, JsonReader.BUFFER_SIZE);
      limitField.setInt(spyReader, newLimit);
      return newLimit >= minimum;
    }).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_trueLowerCase() throws Exception {
    setBufferAndLimit("true ");
    int result = invokePeekKeyword();
    assertEquals(5, result); // PEEKED_TRUE = 5
    // pos should be advanced by 4
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_trueUpperCase() throws Exception {
    setBufferAndLimit("TRUE ");
    int result = invokePeekKeyword();
    assertEquals(5, result);
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_falseLowerCase() throws Exception {
    setBufferAndLimit("false ");
    int result = invokePeekKeyword();
    assertEquals(6, result); // PEEKED_FALSE = 6
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(5, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_falseUpperCase() throws Exception {
    setBufferAndLimit("FALSE ");
    int result = invokePeekKeyword();
    assertEquals(6, result);
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(5, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_nullLowerCase() throws Exception {
    setBufferAndLimit("null ");
    int result = invokePeekKeyword();
    assertEquals(7, result); // PEEKED_NULL = 7
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_nullUpperCase() throws Exception {
    setBufferAndLimit("NULL ");
    int result = invokePeekKeyword();
    assertEquals(7, result);
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_invalidFirstChar() throws Exception {
    setBufferAndLimit("x");
    int result = invokePeekKeyword();
    assertEquals(0, result); // PEEKED_NONE = 0
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    // pos should remain unchanged
    assertEquals(0, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_partialBufferFillFailure() throws Exception {
    // Setup buffer with "tru" only (incomplete "true")
    setBufferAndLimit("tru");
    // Spy and mock fillBuffer to return false (simulate EOF)
    setFillBufferBehavior(false);
    int result = invokePeekKeyword();
    assertEquals(0, result);
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    // pos should remain unchanged
    assertEquals(0, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_partialBufferFillSuccess() throws Exception {
    // Setup buffer with "tru" only (incomplete "true")
    setBufferAndLimit("tru");
    // Spy and mock fillBuffer to return true (simulate buffer refill)
    setFillBufferBehavior(true);
    // Also, simulate buffer extended with 'e' at pos+3
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    buffer[3] = 'e';
    // Increase limit accordingly
    setLimit(4);

    int result = invokePeekKeyword();
    assertEquals(5, result);
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_followedByLiteralChar() throws Exception {
    // Setup buffer with "truex" where 'x' is a literal char -> should fail match
    setBufferAndLimit("truex");
    // Spy and mock fillBuffer to return true to simulate buffer refill if needed
    setFillBufferBehavior(true);
    // 'x' is literal character, so peekKeyword should return PEEKED_NONE
    int result = invokePeekKeyword();
    assertEquals(0, result);
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    // pos should remain unchanged
    assertEquals(0, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_followedByNonLiteralChar() throws Exception {
    // Setup buffer with "true " where space is non-literal char -> should succeed
    setBufferAndLimit("true ");
    int result = invokePeekKeyword();
    assertEquals(5, result);
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    assertEquals(4, posField.getInt(jsonReader));
  }

  @Test
    @Timeout(8000)
  public void testPeekKeyword_fillBufferThrowsIOException() throws Exception {
    setBufferAndLimit("tru");
    // Spy to throw IOException on fillBuffer call
    JsonReader spyReader = spy(jsonReader);
    doThrow(new IOException("fillBuffer failure")).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;

    IOException thrown = assertThrows(IOException.class, () -> invokePeekKeyword());
    assertEquals("fillBuffer failure", thrown.getMessage());
  }
}