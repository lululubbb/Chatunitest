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

public class JsonReader_206_1Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private String invokeNextUnquotedValue() throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    try {
      return (String) method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      // unwrap IOException
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  private void invokeCheckLenient(Object target) throws Exception {
    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);
    checkLenientMethod.invoke(target);
  }

  private boolean invokeFillBuffer(Object target, int n) throws Exception {
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);
    return (boolean) fillBufferMethod.invoke(target, n);
  }

  @Test
    @Timeout(8000)
  public void nextUnquotedValue_simpleLiteralWithinBuffer() throws Exception {
    // Setup buffer with unquoted literal ending with space (stop char)
    char[] buffer = "simpleValue ".toCharArray();
    setInternalState(buffer, 0, buffer.length);
    jsonReader.setLenient(true);

    String result = invokeNextUnquotedValue();

    assertEquals("simpleValue", result);
    assertEquals(buffer.length - 1, getPos());
  }

  @Test
    @Timeout(8000)
  public void nextUnquotedValue_literalStopsAtSpecialChar() throws Exception {
    // Setup buffer with unquoted literal ending with comma (stop char)
    char[] buffer = "value,rest".toCharArray();
    setInternalState(buffer, 0, buffer.length);
    jsonReader.setLenient(true);

    String result = invokeNextUnquotedValue();

    assertEquals("value", result);
    assertEquals("value".length(), getPos());
  }

  @Test
    @Timeout(8000)
  public void nextUnquotedValue_lenientCheckLenientCalledOnSpecialChars() throws Exception {
    // Setup buffer with special characters that call checkLenient()
    char[] buffer = "/;#=rest".toCharArray();
    setInternalState(buffer, 0, buffer.length);
    jsonReader.setLenient(true);

    // Spy on jsonReader
    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "buffer", buffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", buffer.length);

    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    method.invoke(spyReader);

    // Use reflection to verify checkLenient was called by spying on it manually
    // Since checkLenient is private, we cannot verify directly with Mockito
    // Instead, we can override checkLenient via spy to track calls

    // So here we just verify that the method was invoked via Mockito spy invocation count
    // But since checkLenient is private, Mockito cannot intercept it automatically.
    // Instead, we use reflection to replace checkLenient with a spy method

    // The above approach is complicated; simpler alternative is to create a subclass for testing

    // So we do that here:

    class JsonReaderSpy extends JsonReader {
      boolean checkLenientCalled = false;

      JsonReaderSpy(Reader in) {
        super(in);
      }

      @Override
      protected void checkLenient() {
        checkLenientCalled = true;
        try {
          Method m = JsonReader.class.getDeclaredMethod("checkLenient");
          m.setAccessible(true);
          m.invoke(this);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    JsonReaderSpy jsonReaderSpy = new JsonReaderSpy(mockReader);
    setField(jsonReaderSpy, "buffer", buffer);
    setField(jsonReaderSpy, "pos", 0);
    setField(jsonReaderSpy, "limit", buffer.length);
    jsonReaderSpy.setLenient(true);

    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);
    nextUnquotedValueMethod.invoke(jsonReaderSpy);

    assertTrue(jsonReaderSpy.checkLenientCalled);
  }

  @Test
    @Timeout(8000)
  public void nextUnquotedValue_longLiteralUsesStringBuilder() throws Exception {
    // Create a long literal longer than buffer size to force StringBuilder usage
    int length = 1024 + 10;
    char[] longLiteral = new char[length];
    for (int i = 0; i < length - 1; i++) {
      longLiteral[i] = 'a';
    }
    longLiteral[length - 1] = ' '; // stop char
    setInternalState(longLiteral, 0, length);

    jsonReader.setLenient(true);

    String result = invokeNextUnquotedValue();

    assertEquals(length - 1, result.length());
    for (char c : result.toCharArray()) {
      assertEquals('a', c);
    }
    assertEquals(length, getPos());
  }

  @Test
    @Timeout(8000)
  public void nextUnquotedValue_fillBufferReturnsFalseBreaksLoop() throws Exception {
    // Setup buffer partially filled, fillBuffer returns false to break loop
    char[] partialBuffer = "partial".toCharArray();
    setInternalState(partialBuffer, 0, partialBuffer.length);
    jsonReader.setLenient(true);

    // Spy jsonReader and stub fillBuffer via reflection proxy
    JsonReader spyReader = spy(jsonReader);
    setField(spyReader, "buffer", partialBuffer);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", partialBuffer.length);

    // Since fillBuffer is private, we cannot stub it directly with Mockito.
    // Instead, we create a subclass to override fillBuffer.

    class JsonReaderFillBufferStub extends JsonReader {
      boolean fillBufferCalled = false;

      JsonReaderFillBufferStub(Reader in) {
        super(in);
      }

      @Override
      protected boolean fillBuffer(int n) {
        fillBufferCalled = true;
        return false; // always return false to break loop
      }
    }

    JsonReaderFillBufferStub stubReader = new JsonReaderFillBufferStub(mockReader);
    setField(stubReader, "buffer", partialBuffer);
    setField(stubReader, "pos", 0);
    setField(stubReader, "limit", partialBuffer.length);
    stubReader.setLenient(true);

    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    String result = (String) method.invoke(stubReader);

    assertEquals("partial", result);
    assertEquals(partialBuffer.length, getFieldInt(stubReader, "pos"));
    assertTrue(stubReader.fillBufferCalled);
  }

  // Helpers to set private fields
  private void setInternalState(char[] buffer, int pos, int limit) throws Exception {
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", pos);
    setField(jsonReader, "limit", limit);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = getDeclaredField(target.getClass(), fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private int getPos() throws Exception {
    var field = getDeclaredField(JsonReader.class, "pos");
    field.setAccessible(true);
    return (int) field.get(jsonReader);
  }

  private int getFieldInt(Object target, String fieldName) throws Exception {
    var field = getDeclaredField(target.getClass(), fieldName);
    field.setAccessible(true);
    return (int) field.get(target);
  }

  private java.lang.reflect.Field getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}