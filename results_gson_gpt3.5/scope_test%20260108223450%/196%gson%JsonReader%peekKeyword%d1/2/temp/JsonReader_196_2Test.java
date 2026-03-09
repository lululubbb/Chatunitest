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

public class JsonReader_196_2Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Create JsonReader with a dummy Reader (not used in peekKeyword)
    jsonReader = new JsonReader(mock(Reader.class));
  }

  private int invokePeekKeyword() throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("peekKeyword");
    method.setAccessible(true);
    try {
      return (int) method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      // Unwrap IOException
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedTrue_whenBufferContainsTrue() throws Exception {
    setBufferContent("true");
    int result = invokePeekKeyword();
    assertEquals(5, result); // PEEKED_TRUE = 5
    assertEquals(4, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedTrue_whenBufferContainsTRUEUpperCase() throws Exception {
    setBufferContent("TRUE");
    int result = invokePeekKeyword();
    assertEquals(5, result); // PEEKED_TRUE = 5
    assertEquals(4, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedFalse_whenBufferContainsFalse() throws Exception {
    setBufferContent("false");
    int result = invokePeekKeyword();
    assertEquals(6, result); // PEEKED_FALSE = 6
    assertEquals(5, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedFalse_whenBufferContainsFALSEUpperCase() throws Exception {
    setBufferContent("FALSE");
    int result = invokePeekKeyword();
    assertEquals(6, result); // PEEKED_FALSE = 6
    assertEquals(5, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedNull_whenBufferContainsNull() throws Exception {
    setBufferContent("null");
    int result = invokePeekKeyword();
    assertEquals(7, result); // PEEKED_NULL = 7
    assertEquals(4, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedNull_whenBufferContainsNULLUpperCase() throws Exception {
    setBufferContent("NULL");
    int result = invokePeekKeyword();
    assertEquals(7, result); // PEEKED_NULL = 7
    assertEquals(4, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedNone_whenBufferStartsWithNonKeywordChar() throws Exception {
    setBufferContent("x");
    int result = invokePeekKeyword();
    assertEquals(0, result); // PEEKED_NONE = 0
    assertEquals(0, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedNone_whenBufferPartialKeywordAndFillBufferFails() throws Exception {
    setBufferContent("tr");
    // Set limit less than keyword length to force fillBuffer call
    setLimit(2);
    // Override fillBuffer to return false to simulate EOF or no more data
    setFillBufferReturn(false);
    int result = invokePeekKeyword();
    assertEquals(0, result); // PEEKED_NONE = 0
    assertEquals(0, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedNone_whenKeywordDoesNotMatchDueToCharMismatch() throws Exception {
    setBufferContent("tRueX");
    int result = invokePeekKeyword();
    assertEquals(0, result); // PEEKED_NONE = 0
    assertEquals(0, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedNone_whenNextCharIsLiteral() throws Exception {
    // "trueX" where X is a literal character (e.g. letter or digit)
    setBufferContent("trueX");
    // Set limit to buffer length
    setLimit(5);
    // Override isLiteral to return true for 'X'
    setIsLiteralReturn(true);
    int result = invokePeekKeyword();
    assertEquals(0, result); // PEEKED_NONE = 0
    assertEquals(0, getPos());
  }

  @Test
    @Timeout(8000)
  public void peekKeyword_shouldReturnPeekedWhenNextCharIsNotLiteral() throws Exception {
    // "true " space is not literal
    setBufferContent("true ");
    setLimit(5);
    setIsLiteralReturn(false);
    int result = invokePeekKeyword();
    assertEquals(5, result); // PEEKED_TRUE = 5
    assertEquals(4, getPos());
  }

  // Helper methods to set private fields and override private methods

  private void setBufferContent(String content) throws Exception {
    char[] buffer = new char[1024];
    for (int i = 0; i < content.length(); i++) {
      buffer[i] = content.charAt(i);
    }
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", content.length());
  }

  private void setLimit(int limit) throws Exception {
    setField("limit", limit);
  }

  private void setFillBufferReturn(boolean ret) throws Exception {
    // Override fillBuffer(int) to return ret
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    // Use a spy and override fillBuffer method with Mockito
    JsonReader spyReader = spy(jsonReader);
    doReturn(ret).when(spyReader).fillBuffer(anyInt());
    jsonReader = spyReader;
  }

  private void setIsLiteralReturn(boolean ret) throws Exception {
    // Override isLiteral(char) to return ret
    Method isLiteralMethod = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteralMethod.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(ret).when(spyReader).isLiteral(anyChar());
    jsonReader = spyReader;
  }

  private int getPos() throws Exception {
    return (int) getField("pos");
  }

  private Object getField(String name) throws Exception {
    java.lang.reflect.Field field = JsonReader.class.getDeclaredField(name);
    field.setAccessible(true);
    return field.get(jsonReader);
  }

  private void setField(String name, Object value) throws Exception {
    java.lang.reflect.Field field = JsonReader.class.getDeclaredField(name);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }
}