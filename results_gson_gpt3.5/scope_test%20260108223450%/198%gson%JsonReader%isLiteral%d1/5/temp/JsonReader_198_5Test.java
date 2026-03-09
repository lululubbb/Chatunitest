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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_198_5Test {

  private JsonReader jsonReader;
  private Method isLiteralMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    // Create a dummy Reader because JsonReader requires one
    Reader dummyReader = mock(Reader.class);
    jsonReader = new JsonReader(dummyReader);
    isLiteralMethod = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteralMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_returnsFalseForCharsThatTriggerCheckLenient() throws Throwable {
    // These chars call checkLenient() then fall through to return false
    char[] chars = {'/', '\\', ';', '#', '='};
    for (char c : chars) {
      try {
        Object result = isLiteralMethod.invoke(jsonReader, c);
        assertEquals(false, result, "Expected false for char: " + c);
      } catch (InvocationTargetException e) {
        // checkLenient() may throw IOException if lenient is false
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
          // Expected if lenient is false
          assertTrue(cause instanceof IOException);
        } else {
          throw e;
        }
      }
    }
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_returnsFalseForCharsThatReturnFalseDirectly() throws Throwable {
    // These chars directly return false
    char[] chars = {'{', '}', '[', ']', ':', ',', ' ', '\t', '\f', '\r', '\n'};
    for (char c : chars) {
      Object result = isLiteralMethod.invoke(jsonReader, c);
      assertEquals(false, result, "Expected false for char: " + c);
    }
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_returnsTrueForOtherChars() throws Throwable {
    // Test some chars that should return true
    char[] chars = {'a', 'Z', '0', '9', '_', '+', '*', '\"', '\'', '@'};
    for (char c : chars) {
      Object result = isLiteralMethod.invoke(jsonReader, c);
      assertEquals(true, result, "Expected true for char: " + c);
    }
  }

  @Test
    @Timeout(8000)
  void testIsLiteral_checkLenientDoesNotThrowWhenLenientTrue() throws Throwable {
    jsonReader.setLenient(true);
    char[] chars = {'/', '\\', ';', '#', '='};
    for (char c : chars) {
      Object result = isLiteralMethod.invoke(jsonReader, c);
      assertEquals(false, result, "Expected false for char: " + c + " with lenient true");
    }
  }

}