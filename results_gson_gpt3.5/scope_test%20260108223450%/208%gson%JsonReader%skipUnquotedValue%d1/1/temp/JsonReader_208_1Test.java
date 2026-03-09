package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_208_1Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = spy(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_withImmediateStopChar() throws Exception {
    // Arrange: buffer with a stop char at pos
    char[] buffer = getField(jsonReader, "buffer");
    buffer[0] = '{'; // stop char
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 1);

    // Act: invoke private skipUnquotedValue
    invokeSkipUnquotedValue(jsonReader);

    // Assert: pos should stay 0 because i=0 and pos+=i returns immediately
    assertEquals(0, (int) getField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_withStopCharAfterSeveralChars() throws Exception {
    // Arrange: buffer with chars until stop char at index 3
    char[] buffer = getField(jsonReader, "buffer");
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = 'c';
    buffer[3] = ','; // stop char
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 5);

    // Act
    invokeSkipUnquotedValue(jsonReader);

    // Assert pos advanced by 3 (index of stop char)
    assertEquals(3, (int) getField(jsonReader, "pos"));
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_withNoStopCharAndFillBufferTrue() throws Exception {
    // Arrange: buffer with no stop chars in current limit, fillBuffer returns true once then false
    char[] buffer = getField(jsonReader, "buffer");
    for (int i = 0; i < 5; i++) {
      buffer[i] = 'a';
    }
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 5);

    // Spy on jsonReader to mock fillBuffer
    JsonReader spyReader = spy(jsonReader);

    // fillBuffer is private, so mock via reflection and doAnswer
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);

    // Use doAnswer to mock fillBuffer behavior
    final boolean[] returnedTrue = {false};
    doAnswer(invocation -> {
      if (!returnedTrue[0]) {
        returnedTrue[0] = true;
        // simulate filling buffer: set limit to 10, pos stays 0
        setField(spyReader, "limit", 10);
        return true;
      } else {
        return false;
      }
    }).when(spyReader).getClass()
      .getDeclaredMethod("fillBuffer", int.class)
      .invoke(spyReader, 1);

    // Because Mockito cannot mock private methods directly, we use spy + reflection to invoke skipUnquotedValue on spyReader,
    // and override fillBuffer via reflection proxy below:

    // Instead of mocking fillBuffer via Mockito (which can't mock private methods), use a subclass to override fillBuffer:
    JsonReader spyWithFillBuffer = new JsonReader(mockReader) {
      private int callCount = 0;

      @Override
      protected boolean fillBuffer(int minimum) throws IOException {
        callCount++;
        if (callCount == 1) {
          setField(this, "limit", 10);
          return true;
        }
        return false;
      }
    };
    // Copy buffer, pos, limit from original spyReader to spyWithFillBuffer
    char[] originalBuffer = getField(spyReader, "buffer");
    System.arraycopy(originalBuffer, 0, getField(spyWithFillBuffer, "buffer"), 0, originalBuffer.length);
    setField(spyWithFillBuffer, "pos", 0);
    setField(spyWithFillBuffer, "limit", 5);

    // Act
    invokeSkipUnquotedValue(spyWithFillBuffer);

    // Assert pos advanced by 10 (5 + 5)
    // Because loop runs twice, each time pos += i (which is 5)
    assertEquals(10, (int) getField(spyWithFillBuffer, "pos"));
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_withLenientCheckLenientCalled() throws Exception {
    // Arrange: buffer with stop char requiring checkLenient call ('/')
    char[] buffer = getField(jsonReader, "buffer");
    buffer[0] = '/'; // triggers checkLenient
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 1);

    // Spy to verify checkLenient called
    JsonReader spyReader = spy(jsonReader);

    // Because checkLenient is private, use doNothing via reflection proxy:
    doNothing().when(spyReader).getClass()
      .getDeclaredMethod("checkLenient")
      .invoke(spyReader);

    // Instead, create subclass to override checkLenient:
    JsonReader spyWithCheckLenient = new JsonReader(mockReader) {
      boolean called = false;

      @Override
      protected void checkLenient() {
        called = true;
      }
    };

    // Copy buffer, pos, limit from jsonReader to spyWithCheckLenient
    char[] originalBuffer = getField(jsonReader, "buffer");
    System.arraycopy(originalBuffer, 0, getField(spyWithCheckLenient, "buffer"), 0, originalBuffer.length);
    setField(spyWithCheckLenient, "pos", 0);
    setField(spyWithCheckLenient, "limit", 1);

    // Act
    invokeSkipUnquotedValue(spyWithCheckLenient);

    // Assert checkLenient called once
    assertEquals(true, getField(spyWithCheckLenient, "called"));
    // pos should remain 0 (i=0)
    assertEquals(0, (int) getField(spyWithCheckLenient, "pos"));
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_withLenientCheckLenientFallthrough() throws Exception {
    // Arrange: buffer with stop char requiring checkLenient ('='), followed by a stop char that returns
    char[] buffer = getField(jsonReader, "buffer");
    buffer[0] = '='; // triggers checkLenient and fallthrough
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 1);

    // Subclass to override checkLenient and track call count
    JsonReader spyWithCheckLenient = new JsonReader(mockReader) {
      int callCount = 0;

      @Override
      protected void checkLenient() {
        callCount++;
      }
    };

    // Copy buffer, pos, limit from jsonReader to spyWithCheckLenient
    char[] originalBuffer = getField(jsonReader, "buffer");
    System.arraycopy(originalBuffer, 0, getField(spyWithCheckLenient, "buffer"), 0, originalBuffer.length);
    setField(spyWithCheckLenient, "pos", 0);
    setField(spyWithCheckLenient, "limit", 1);

    // Act
    invokeSkipUnquotedValue(spyWithCheckLenient);

    // Assert checkLenient called once
    assertEquals(1, (int) getField(spyWithCheckLenient, "callCount"));
    // pos should remain 0
    assertEquals(0, (int) getField(spyWithCheckLenient, "pos"));
  }

  private void invokeSkipUnquotedValue(JsonReader reader) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    method.setAccessible(true);
    method.invoke(reader);
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String fieldName) throws Exception {
    java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}