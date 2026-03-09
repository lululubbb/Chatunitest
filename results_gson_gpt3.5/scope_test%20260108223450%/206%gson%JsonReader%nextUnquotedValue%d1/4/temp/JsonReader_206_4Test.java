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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_206_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_simpleLiteral() throws Exception {
    // Setup buffer with unquoted string ending with a delimiter (comma)
    setPrivateField(jsonReader, "buffer", "abc,def".toCharArray());
    setPrivateField(jsonReader, "pos", 0);
    setPrivateField(jsonReader, "limit", 7);

    String result = invokeNextUnquotedValue(jsonReader);

    assertEquals("abc", result);
    assertEquals(3, getPrivateIntField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_withWhitespaceDelimiter() throws Exception {
    setPrivateField(jsonReader, "buffer", "abc def".toCharArray());
    setPrivateField(jsonReader, "pos", 0);
    setPrivateField(jsonReader, "limit", 7);

    String result = invokeNextUnquotedValue(jsonReader);

    assertEquals("abc", result);
    assertEquals(3, getPrivateIntField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_withLenientCheckLenientCall() throws Exception {
    // Setup buffer with a character that triggers checkLenient call: '/'
    setPrivateField(jsonReader, "buffer", "abc/def".toCharArray());
    setPrivateField(jsonReader, "pos", 0);
    setPrivateField(jsonReader, "limit", 7);
    jsonReader.setLenient(true);

    // Spy on jsonReader
    JsonReader spyReader = spy(jsonReader);

    // Use reflection to spy on private checkLenient() method call count
    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);

    // Use a flag to count invocations
    InvocationCounter counter = new InvocationCounter();
    doAnswer(invocation -> {
      counter.count++;
      return invocation.callRealMethod();
    }).when(spyReader, "checkLenient");

    String result = invokeNextUnquotedValue(spyReader);

    assertEquals("abc", result);
    assertTrue(counter.count >= 1, "checkLenient should be called at least once");
    assertEquals(3, getPrivateIntField(spyReader, "pos"));
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_bufferRefillAndStringBuilderUsed() throws Exception {
    // Setup buffer full with no delimiter to force builder usage and buffer refill
    char[] buffer = new char[JsonReader.BUFFER_SIZE];
    for (int i = 0; i < buffer.length; i++) {
      buffer[i] = 'a';
    }
    setPrivateField(jsonReader, "buffer", buffer);
    setPrivateField(jsonReader, "pos", 0);
    setPrivateField(jsonReader, "limit", buffer.length);

    // Spy on jsonReader
    JsonReader spyReader = spy(jsonReader);

    // Mock fillBuffer to simulate buffer refill: first call returns true, second false
    doAnswer(new FillBufferAnswer(spyReader)).when(spyReader).fillBuffer(anyInt());

    String result = invokeNextUnquotedValue(spyReader);

    // The result should be a string of 'a's + 'b's (buffer size * 2)
    assertEquals(JsonReader.BUFFER_SIZE * 2, result.length());
    for (int i = 0; i < JsonReader.BUFFER_SIZE; i++) {
      assertEquals('a', result.charAt(i));
    }
    for (int i = JsonReader.BUFFER_SIZE; i < result.length(); i++) {
      assertEquals('b', result.charAt(i));
    }
  }

  @Test
    @Timeout(8000)
  void testNextUnquotedValue_endOfBufferWithoutDelimiter() throws Exception {
    // Buffer with no delimiter and fillBuffer returns false immediately
    char[] buffer = "abcdefg".toCharArray();
    setPrivateField(jsonReader, "buffer", buffer);
    setPrivateField(jsonReader, "pos", 0);
    setPrivateField(jsonReader, "limit", buffer.length);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(anyInt());

    String result = invokeNextUnquotedValue(spyReader);

    assertEquals("abcdefg", result);
    assertEquals(buffer.length, getPrivateIntField(spyReader, "pos"));
  }

  private String invokeNextUnquotedValue(JsonReader reader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    return (String) method.invoke(reader);
  }

  private void setPrivateField(Object target, String fieldName, Object value) {
    try {
      Field field = getDeclaredField(target.getClass(), fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private int getPrivateIntField(Object target, String fieldName) {
    try {
      Field field = getDeclaredField(target.getClass(), fieldName);
      field.setAccessible(true);
      return field.getInt(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Field getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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

  // Helper class to count invocations
  private static class InvocationCounter {
    int count = 0;
  }

  // Custom Answer to mock fillBuffer(int) private method via reflection
  private static class FillBufferAnswer implements org.mockito.stubbing.Answer<Boolean> {
    private final JsonReader target;
    private boolean firstCall = true;

    FillBufferAnswer(JsonReader target) {
      this.target = target;
    }

    @Override
    public Boolean answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
      int minimum = invocation.getArgument(0);
      if (firstCall && minimum == JsonReader.BUFFER_SIZE + 1) {
        firstCall = false;
        char[] newBuffer = new char[JsonReader.BUFFER_SIZE];
        for (int i = 0; i < newBuffer.length; i++) {
          newBuffer[i] = 'b';
        }
        setField(target, "buffer", newBuffer);
        setField(target, "pos", 0);
        setField(target, "limit", newBuffer.length);
        return true;
      }
      return false;
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
      Field field = obj.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(obj, value);
    }
  }

  // Workaround to spy private methods using reflection and Mockito
  private static <T> OngoingStubbing<T> doAnswer(org.mockito.stubbing.Answer<?> answer, JsonReader spy, String methodName, Class<?>... parameterTypes) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod(methodName, parameterTypes);
    method.setAccessible(true);
    return doAnswer(answer).when(spy);
  }
}