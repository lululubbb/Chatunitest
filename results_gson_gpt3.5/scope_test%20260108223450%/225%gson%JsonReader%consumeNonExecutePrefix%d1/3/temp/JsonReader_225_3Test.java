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

import com.google.gson.stream.JsonReader;

class JsonReader_225_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  /**
   * Helper method to invoke private consumeNonExecutePrefix() via reflection.
   */
  private void invokeConsumeNonExecutePrefix() throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("consumeNonExecutePrefix");
    method.setAccessible(true);
    try {
      method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      // unwrap IOException if thrown
      if (e.getCause() instanceof IOException) {
        throw e.getCause();
      }
      throw e;
    }
  }

  /**
   * Test case: buffer contains the security token at current position.
   * Expected: pos is advanced by 5.
   */
  @Test
    @Timeout(8000)
  void consumeNonExecutePrefix_securityToken_consumesToken() throws Throwable {
    // Arrange: position and limit set so buffer has at least 5 chars after pos
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 10);
    char[] buffer = new char[1024];
    buffer[0] = ')';
    buffer[1] = ']';
    buffer[2] = '}';
    buffer[3] = '\'';
    buffer[4] = '\n';
    setField(jsonReader, "buffer", buffer);

    // Mock nextNonWhitespace(true) to do nothing but decrement pos by 1 after call
    // We cannot mock private method, so simulate by setting pos after call
    // So call will decrement pos by 1, so pos after nextNonWhitespace = -1, then pos-- => -2
    // Instead, we simulate nextNonWhitespace(true) call by setting pos to 1 before invoke
    setField(jsonReader, "pos", 1);

    // Mock fillBuffer(5) to true to avoid early return
    // We cannot mock private fillBuffer, so set limit and pos accordingly to avoid fillBuffer call
    // pos + 5 = 6 <= limit = 10, so fillBuffer not called

    // Act
    invokeConsumeNonExecutePrefix();

    // Assert pos advanced by 5 from initial pos-1 (pos was 1 before method, method decrements pos by 1 to 0)
    // then if security token found pos +=5 => pos = 5
    int pos = (int) getField(jsonReader, "pos");
    assertEquals(5, pos);
  }

  /**
   * Test case: buffer does not contain the security token at current position.
   * Expected: pos is decremented by 1 but not advanced by 5.
   */
  @Test
    @Timeout(8000)
  void consumeNonExecutePrefix_noSecurityToken_noConsume() throws Throwable {
    // Arrange
    setField(jsonReader, "pos", 1);
    setField(jsonReader, "limit", 10);
    char[] buffer = new char[1024];
    buffer[1] = 'x'; // does not match ')'
    buffer[2] = ']';
    buffer[3] = '}';
    buffer[4] = '\'';
    buffer[5] = '\n';
    setField(jsonReader, "buffer", buffer);

    // Act
    invokeConsumeNonExecutePrefix();

    // Assert pos decremented by 1 from initial pos (1 -> 0), no advance by 5
    int pos = (int) getField(jsonReader, "pos");
    assertEquals(0, pos);
  }

  /**
   * Test case: pos + 5 > limit and fillBuffer(5) returns false.
   * Expected: method returns early and pos is decremented by 1 only.
   */
  @Test
    @Timeout(8000)
  void consumeNonExecutePrefix_posPlus5GreaterThanLimitFillBufferFalse_returnsEarly() throws Throwable {
    // Arrange
    setField(jsonReader, "pos", 6);
    setField(jsonReader, "limit", 10);
    // pos + 5 = 11 > limit(10), so fillBuffer(5) called
    // We cannot mock private fillBuffer, so simulate by setting limit < pos+5 and pos+5 > limit
    // and override fillBuffer to return false using subclass

    JsonReader jsonReaderSpy = new JsonReader(mockReader) {
      @Override
      protected boolean fillBuffer(int minimum) {
        return false;
      }
    };
    setField(jsonReaderSpy, "pos", 6);
    setField(jsonReaderSpy, "limit", 10);

    // Set buffer with arbitrary chars
    char[] buffer = new char[1024];
    setField(jsonReaderSpy, "buffer", buffer);

    // Act
    Method method = JsonReader.class.getDeclaredMethod("consumeNonExecutePrefix");
    method.setAccessible(true);
    try {
      method.invoke(jsonReaderSpy);
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof IOException) {
        throw e.getCause();
      }
      throw e;
    }

    // Assert pos decremented by 1 only
    int pos = (int) getField(jsonReaderSpy, "pos");
    assertEquals(5, pos);
  }

  /**
   * Test case: nextNonWhitespace(true) throws IOException.
   * Expected: exception is propagated.
   */
  @Test
    @Timeout(8000)
  void consumeNonExecutePrefix_nextNonWhitespaceThrows_throwsIOException() throws Throwable {
    // Arrange
    JsonReader jsonReaderSpy = new JsonReader(mockReader) {
      @Override
      protected int nextNonWhitespace(boolean throwOnEof) throws IOException {
        throw new IOException("forced");
      }
    };

    // Act & Assert
    Method method = JsonReader.class.getDeclaredMethod("consumeNonExecutePrefix");
    method.setAccessible(true);
    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        method.invoke(jsonReaderSpy);
      } catch (InvocationTargetException e) {
        if (e.getCause() instanceof IOException) {
          throw e.getCause();
        }
        throw e;
      }
    });
    assertEquals("forced", thrown.getMessage());
  }

  // Utility methods for reflection field access

  private static Object getField(Object target, String fieldName) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}