package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_198_4Test {

  private JsonReader jsonReader;
  private Method isLiteralMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    jsonReader = new JsonReader(mock(java.io.Reader.class));
    isLiteralMethod = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteralMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_returnsFalseForSpecialCharsWithoutLenient() throws Throwable {
    // Characters that return false without lenient and do not call checkLenient()
    char[] chars = { '{', '}', '[', ']', ':', ',', ' ', '\t', '\f', '\r', '\n' };
    for (char c : chars) {
      boolean result = invokeIsLiteral(c);
      assertFalse(result, "Expected false for char: " + (int) c);
    }
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_returnsFalseAndCallsCheckLenientForLenientChars() throws Throwable {
    // Characters that call checkLenient() then return false
    // We subclass JsonReader to override checkLenient() and track calls

    class TestJsonReader extends JsonReader {
      boolean checkLenientCalled = false;

      TestJsonReader(java.io.Reader in) {
        super(in);
        setLenient(true);
      }

      void callCheckLenient() throws IOException {
        checkLenientCalled = true;
      }
    }

    TestJsonReader testReader = new TestJsonReader(mock(java.io.Reader.class));
    Method testIsLiteral = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    testIsLiteral.setAccessible(true);

    // Replace the checkLenient method on testReader instance using reflection
    // Because checkLenient is private and final, we cannot override it directly,
    // so we use a spy to intercept calls.

    JsonReader spyReader = spy(testReader);

    // Use reflection to get the real checkLenient method
    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);

    // Override checkLenient in spy to set flag
    doAnswer(invocation -> {
      testReader.checkLenientCalled = true;
      return null;
    }).when(spyReader).checkLenient();

    char[] chars = { '/', '\\', ';', '#', '=' };
    for (char c : chars) {
      testReader.checkLenientCalled = false;
      boolean result = (boolean) testIsLiteral.invoke(spyReader, c);
      assertFalse(result, "Expected false for char: " + (int) c);
      assertTrue(testReader.checkLenientCalled, "Expected checkLenient() to be called for char: " + (int) c);
    }
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_returnsTrueForDefaultChars() throws Throwable {
    // Characters that should return true
    char[] chars = { 'a', 'Z', '0', '9', '_', '+', '@', '%', '*', '!' };
    for (char c : chars) {
      boolean result = invokeIsLiteral(c);
      assertTrue(result, "Expected true for char: " + c);
    }
  }

  private boolean invokeIsLiteral(char c) throws Throwable {
    try {
      return (boolean) isLiteralMethod.invoke(jsonReader, c);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}